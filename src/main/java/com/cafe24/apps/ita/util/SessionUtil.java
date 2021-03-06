package com.cafe24.apps.ita.util;

import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.dto.UserDto;
import com.cafe24.apps.ita.entity.User;

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

        UserDto userDto = (UserDto) session.getAttribute(session.getId());
        return userDto != null && !userDto.getUserId().isEmpty();
    }

    /**
     * 세션에 회원 정보 등록
     *
     * @param session
     * @param user
     */
    public static void setUserInfo(HttpSession session, User user) {
        session.setAttribute(session.getId(), user.convertDto());
    }

    /**
     * 세션 회원 정보 조회
     *
     * @param session
     */
    public static UserDto getUserInfo(HttpSession session) {
        return (UserDto) session.getAttribute(session.getId());
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
    public static MallDto getMallInfo(HttpSession session) throws Exception {
        Optional<MallDto> mallInfo = Optional.ofNullable((MallDto) session.getAttribute("mallInfo"));
        mallInfo.orElseThrow(() -> new Exception("mall 정보 없음"));

        return mallInfo.get();
    }
}
