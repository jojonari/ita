package com.cafe24.apps.ita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    /**
     * 메인 페이지
     * @return
     */
    @GetMapping("/")
    public String index() {

        return "/main";
    }

    /**
     * 메인페이지
     * @return
     */
    @GetMapping("/main")
    public String main() {

        return "/main";
    }
}
