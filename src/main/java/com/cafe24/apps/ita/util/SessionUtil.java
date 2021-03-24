package com.cafe24.apps.ita.util;

import com.cafe24.apps.ita.dto.AuthMallDto;
import com.cafe24.apps.ita.dto.PrivateUserDto;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {
    /**
     * 로그인 여부 확인
     * 로그인 : true
     *
     * @param session
     * @return
     */
    public static boolean isSignIn(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return false;
        }

        PrivateUserDto privateUserDto = (PrivateUserDto) session.getAttribute(session.getId());
        return privateUserDto != null && !privateUserDto.getUserId().isEmpty();
    }

    /**
     * 세션에 회원 정보 등록
     *
     * @param session
     * @param privateUserDto
     */
    public static void setUserInfo(HttpSession session, PrivateUserDto privateUserDto) {
        session.setAttribute(session.getId(), privateUserDto);
    }

    /**
     * 세션 회원 정보 조회
     *
     * @param session
     */
    public static PrivateUserDto getUserInfo(HttpSession session) {
        return (PrivateUserDto) session.getAttribute(session.getId());
    }

    /**
     * 세션 초기화
     *
     * @param session
     */
    public static void deleteUserInfo(HttpSession session) {
        session.invalidate();
    }

    /**
     * mall info 조회
     *
     * @param session
     * @return
     */
    public static AuthMallDto getMallInfo(HttpSession session) throws Exception {
        Optional<AuthMallDto> mallInfo = Optional.ofNullable((AuthMallDto) session.getAttribute("mallInfo"));
        mallInfo.orElseThrow(() -> new Exception("mall 정보 없음"));

        return mallInfo.get();
    }
}
