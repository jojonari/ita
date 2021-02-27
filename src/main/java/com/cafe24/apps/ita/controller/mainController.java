package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class mainController {

    /**
     * 메인 페이지
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(HttpSession session, ModelAndView mv) throws Exception {
        if (SessionUtil.isSignIn(session)) {
            mv.addObject("user", SessionUtil.getUserInfo(session));
            mv.setViewName("/main");

            return mv;
        }

        mv.setViewName("/user/sign-in");

        return mv;
    }

    /**
     * 메인페이지
     *
     * @return
     */
    @GetMapping("/main")
    public ModelAndView main(HttpSession session, ModelAndView mv, @RequestParam @Nullable String clientId) {
        if (clientId != null && !clientId.isBlank()) {
            mv.addObject("cleintId", clientId);
        }

        if (SessionUtil.isSignIn(session)) {
            mv.addObject("user", SessionUtil.getUserInfo(session));
            mv.setViewName("/main");

            return mv;
        }

        mv.setViewName("/user/sign-in");

        return mv;
    }

}
