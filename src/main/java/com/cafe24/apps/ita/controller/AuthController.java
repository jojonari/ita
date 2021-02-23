package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
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
        //timestamp 검증
        if (!authService.checkTimestamp(mallDto)) {
            model.addAttribute("error_msg", "timestamp 검증에 실패했습니다.");
            model.addAttribute("error_data", mallDto.toString());

            return "error";
        }

        //hmac 검증
        if (!authService.checkHmac(appIdx, request.getQueryString())) {
            model.addAttribute("error_msg", "hmac 검증에 실패했습니다.");
            model.addAttribute("error_data", mallDto.toString());

            return "error";
        }

        //세션에 저장
        session.setAttribute("mallInfo", mallDto);

        return null;
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
}
