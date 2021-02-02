package com.cafe24.apps.ita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class ItaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItaApplication.class, args);
    }

}
