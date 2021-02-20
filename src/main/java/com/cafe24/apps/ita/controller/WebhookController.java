package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/{appIdx}")
    public Response webhook(@PathVariable Long appIdx) {
        return null;
    }
}
