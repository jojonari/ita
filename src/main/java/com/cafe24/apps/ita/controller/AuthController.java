package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AuthMallDto;
import com.cafe24.apps.ita.dto.CodeDto;
import com.cafe24.apps.ita.dto.PrivateAppDto;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.AuthService;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AppService appService;

    public AuthController(AuthService authService, AppService appService) {
        this.authService = authService;
        this.appService = appService;
    }

    /**
     * app url
     *
     * @param session
     * @param appIdx
     * @param authMallDto
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/{appIdx}")
    public String auth(@PathVariable Long appIdx, @Valid AuthMallDto authMallDto, HttpSession session, Model model, HttpServletRequest request) throws Exception {
        if (!SessionUtil.isSignIn(session)) {
            String callbackUrl = request.getRequestURI() + "?" + request.getQueryString();
            model.addAttribute("callbackUrl", callbackUrl);

            return "/user/sign-in";
        }

        if (!authService.checkTimestamp(authMallDto)) {
            return setError(model, "timestamp 검증에 실패했습니다.", authMallDto);
        }

        if (!authService.checkHmac(appIdx, request.getQueryString())) {
            return setError(model, "hmac 검증에 실패했습니다.", authMallDto);
        }

        PrivateAppDto privateAppDto = appService.getApp(session, appIdx);
        if (privateAppDto == null) {
            return setError(model, "등록된 client가 없습니다.", authMallDto);
        }

        String url = "https://" + request.getServerName() + request.getContextPath() + "/main?clientId=" + privateAppDto.getClientId();

        if (privateAppDto.isClientCredentials()) {
            return "redirect:" + url;
        }

        boolean isExpire = authService.isExpireRefreshToken(privateAppDto, authMallDto.getMall_id());
        if (isExpire || privateAppDto.isRefresh()) {
            //세션에 저장
            session.setAttribute("mallInfo", authMallDto);
            if (privateAppDto.isAuthorizationCode()) {
                //code인증 방식의 경우 - çode 발급 url 호출
                return authService.getCodeRedirectUrl(privateAppDto, authMallDto, request);
            }
        }

        return "redirect:" + url;
    }

    /**
     * code redirect url
     *
     * @param appIdx
     * @return
     */
    @GetMapping("/{appIdx}/redirect")
    public String redirect(@PathVariable Long appIdx, CodeDto codeDto, Model model, HttpServletRequest request) throws Exception {
        if (codeDto.getCode() == null || codeDto.getState() == null) {
            return setError(model, "API 인증 오류", codeDto);
        }

        if (!request.getSession().getId().equals(codeDto.getState())) {
            return setError(model, "state 변조", codeDto);
        }

        PrivateAppDto privateAppDto = appService.getApp(request.getSession(), appIdx);
        if (privateAppDto == null) {
            return setError(model, "등록된 client가 없습니다.", codeDto);
        }

        AccessToken accessToken = authService.getAccessToken(privateAppDto, codeDto, request);

        String url = "https://" + request.getServerName() + request.getContextPath() + "/main?clientId=" + accessToken.convertDto().getClient_id();
        return "redirect:" + url;
    }

    /**
     * 오류 처리
     *
     * @param data
     * @param model
     * @param errorMessage
     * @return
     */
    private String setError(Model model, String errorMessage, Object data) {
        model.addAttribute("error_msg", errorMessage);
        model.addAttribute("error_data", data.toString());

        return "error";
    }
}
