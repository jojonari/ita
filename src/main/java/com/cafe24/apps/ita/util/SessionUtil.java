package com.cafe24.apps.ita.util;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static boolean isLogin(HttpSession session) {
        if (session.getAttribute("IS_LOGIN") == null) {
            return false;
        }

        return (boolean) session.getAttribute("IS_LOGIN");
    }
}
