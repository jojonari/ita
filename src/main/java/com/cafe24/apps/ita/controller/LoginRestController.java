package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.Login;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginRestController {

    @PostMapping("/login")
    public String dologin(HttpServletRequest request, @RequestBody Login login) {

        return "success";
    }
}
