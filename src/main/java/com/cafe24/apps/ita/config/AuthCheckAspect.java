package com.cafe24.apps.ita.config;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Aspect
@Component
@Log4j2
public class AuthCheckAspect {

    /**
     * 로그인을 체크한다.
     */
    @Before("execution(* com.cafe24.apps.ita.controller.*.*(..))")
    public void memberLoginCheck(JoinPoint joinPoint) {
        System.out.println("AOP - Member Login Check Started");

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();

        if (session.getId() == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {
            };
        }
    }
}