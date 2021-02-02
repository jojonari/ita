package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (SessionUtil.isLogin(session)) {
            return "main";
        }
        return "login";
    }


}
