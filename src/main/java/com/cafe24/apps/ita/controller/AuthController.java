package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.CodeDto;
import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
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
     * @param mallDto
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/{appIdx}")
    public String auth(@PathVariable Long appIdx, @Valid MallDto mallDto, HttpSession session, Model model, HttpServletRequest request) throws Exception {
        if (!SessionUtil.isSignIn(session)) {
            String callbackUrl = request.getRequestURI() + "?" + request.getQueryString();
            model.addAttribute("callbackUrl", callbackUrl);

            return "/user/sign-in";
        }

        if (!authService.checkTimestamp(mallDto)) {
            return setError(model, "timestamp 검증에 실패했습니다.", mallDto);
        }

        if (!authService.checkHmac(appIdx, request.getQueryString())) {
            return setError(model, "hmac 검증에 실패했습니다.", mallDto);
        }

        App app = appService.getApp(session, appIdx);
        if (app == null) {
            return setError(model, "등록된 client가 없습니다.", mallDto);
        }
        AppDto appDto = app.convertDto();
        boolean isExpire = authService.isExpireRefreshToken(app, mallDto);
        if (isExpire || app.isRefresh()) {
            //세션에 저장
            session.setAttribute("mallInfo", mallDto);
            return authService.getCodeRedirectUrl(appDto, mallDto, request);
        }

        String url = "https://" + request.getServerName() + request.getContextPath() + "/main?clientId=" + appDto.getClientId();
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

        App app = appService.getApp(request.getSession(), appIdx);
        if (app == null) {
            return setError(model, "등록된 client가 없습니다.", codeDto);
        }

        AccessToken accessToken = authService.getAccessToken(app, codeDto, request);
        authService.saveAccessToken(accessToken);

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
