package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.SignIn;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.service.UserService;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 화면
     *
     * @return
     */
    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    /**
     * 회원가입
     *
     * @param session
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/sign-up")
    public String signUp(HttpSession session, User user) throws NoSuchAlgorithmException {
        User regisertUser = userService.regisertUser(user);
        SessionUtil.setUserInfo(session, regisertUser);

        return "main";
    }

    /**
     * 회원 로그인 화면
     *
     * @param session
     * @return
     */
    @GetMapping("/sign-in")
    public String signIn(HttpSession session) {
        if (SessionUtil.isLogin(session)) {
            System.out.println("이미 로그인 한 회원입니다.");
            return "main";
        }

        return "sign-in";
    }

    /**
     * 회원 로그아웃
     *
     * @param session
     * @return
     */
    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        SessionUtil.deleteUserInfo(session);

        return "sign-in";
    }

    /**
     * 회원 로그인
     *
     * @param session
     * @param signIn
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sign-in")
    public String doLogin(HttpSession session, SignIn signIn) throws Exception {
        Optional<User> optionalUser = userService.getUser(signIn.getUserId());
        if (optionalUser.isEmpty()) {
            System.out.println("회원 정보가 없습니다.");
            return "sign-in";
        }

        User user = optionalUser.get();
        boolean isLogin = userService.doLogin(signIn, user);
        if (!isLogin) {
            System.out.println("패스워드를 틀렸습니다.");
            return "sign-in";
        }

        SessionUtil.setUserInfo(session, user);

        return "main";
    }
}
