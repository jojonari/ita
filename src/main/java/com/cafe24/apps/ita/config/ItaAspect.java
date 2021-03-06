package com.cafe24.apps.ita.config;

import com.cafe24.apps.ita.util.SessionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Aspect
@Component
public class ItaAspect {

    /**
     * 로그인을 체크한다.
     */
    @Before("execution(* com.cafe24.apps.ita.controller.mainController.*(..))")
    public void memberLoginCheck(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();

        //로그인 체크
        if (!SessionUtil.isSignIn(session)) {
//            Todo : 개발중일때만 주석 - 로그인화면으로 보내야 할 것 같다.
//            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {};
        }
    }
}