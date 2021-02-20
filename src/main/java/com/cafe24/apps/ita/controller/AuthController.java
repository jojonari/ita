package com.cafe24.apps.ita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/{appIdx}")
    public ModelAndView auth(@PathVariable Long appIdx) {
        return null;
    }

    @GetMapping("/{appIdx}/code")
    public ModelAndView code(@PathVariable Long appIdx) {
        return null;
    }

    @GetMapping("/{appIdx}/token")
    public ModelAndView token(@PathVariable Long appIdx) {
        return null;
    }
}
