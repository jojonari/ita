package com.cafe24.apps.ita.util;

import com.cafe24.apps.ita.entity.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    /**
     * 로그인 여부 확인
     *
     * @param session
     * @return
     */
    public static boolean isSignIn(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return false;
        }

        User user = (User) session.getAttribute(session.getId());
        if (user.getUserId().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * 세션에 회원 정보 등록
     *
     * @param session
     * @param user
     */
    public static void setUserInfo(HttpSession session, User user) {
        user.deleteUserPw();
        session.setAttribute(session.getId(), user);
    }

    /**
     * 세션 회원 정보 조회
     *
     * @param session
     */
    public static User getUserInfo(HttpSession session) {
        return  (User) session.getAttribute(session.getId());
    }

    /**
     * 세션 초기화
     * @param session
     */
    public static void deleteUserInfo(HttpSession session) {
        session.invalidate();
    }
}
