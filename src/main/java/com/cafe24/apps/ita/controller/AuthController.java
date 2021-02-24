package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.AuthService;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
            return setError(model, "등록된 Client가 없습니다.", mallDto);
        }

        boolean isExpire = authService.isExpireRefreshToken(app, mallDto);
        if (isExpire) {
            //세션에 저장
            session.setAttribute("mallInfo", mallDto);
            return authService.getCodeRedirectUrl(app, mallDto, request);
        }

        return "redirect:/main";
    }

    /**
     * code redirect url
     *
     * @param appIdx
     * @return
     */
    @GetMapping("/{appIdx}/code")
    public ModelAndView code(@PathVariable Long appIdx) {
        return null;
    }

    /**
     * token redirect url
     *
     * @param appIdx
     * @return
     */
    @GetMapping("/{appIdx}/token")
    public ModelAndView token(@PathVariable Long appIdx) {
        return null;
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
