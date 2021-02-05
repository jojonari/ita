package com.cafe24.apps.ita.config;

import com.cafe24.apps.ita.util.SessionUtil;
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
public class AuthCheckAspect {

    /**
     * 로그인을 체크한다.
     */
    @Before("execution(* com.cafe24.apps.ita.controller.*.*(..)) && !@annotation(com.cafe24.apps.ita.util.WithoutSession)")
    public void memberLoginCheck(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();

        //로그인 체크
        if (!SessionUtil.isLogin(session)) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {
            };
        }
    }
}