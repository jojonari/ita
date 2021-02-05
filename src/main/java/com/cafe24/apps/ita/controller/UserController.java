package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.SignIn;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.service.UserService;
import com.cafe24.apps.ita.util.SessionUtil;
import com.cafe24.apps.ita.util.WithoutSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Log4j2
@WithoutSession
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
    @WithoutSession
    @GetMapping("/sign-up")
    public String signUp() {
        return "/user/sign-up";
    }

    /**
     * 회원가입
     *
     * @param session
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     */
    @WithoutSession
    @PostMapping(value = "/sign-up")
    public String signUp(HttpSession session, User user) throws NoSuchAlgorithmException {
        User regisertUser = userService.regisertUser(user);
        SessionUtil.setUserInfo(session, regisertUser);

        return "/main";
    }

    /**
     * 회원 로그인 화면
     *
     * @param session
     * @return
     */
    @WithoutSession
    @GetMapping("/sign-in")
    public String signIn(HttpSession session) {
        if (SessionUtil.isLogin(session)) {
            return "/main";
        }

        return "/user/sign-in";
    }

    /**
     * 회원 로그아웃
     *
     * @param session
     * @return
     */
    @WithoutSession
    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        SessionUtil.deleteUserInfo(session);

        return "/user/sign-in";
    }

    /**
     * 회원 로그인
     *
     * @param session
     * @param signIn
     * @return
     * @throws Exception
     */
    @WithoutSession
    @PostMapping(value = "/sign-in")
    public ModelAndView doLogin(HttpSession session, SignIn signIn) throws Exception {
        Optional<User> optionalUser = userService.getUser(signIn.getUserId());
        ModelAndView mv = new ModelAndView();
        if (optionalUser.isEmpty()) {
            mv.setViewName("/user/sign-in");
            mv.addObject("error_msg", "회원 정보가 없습니다.");
            return mv;
        }

        User user = optionalUser.get();
        boolean isLogin = userService.doLogin(signIn, user);
        if (!isLogin) {
            mv.setViewName("/user/sign-in");
            mv.addObject("error_msg", "패스워드를 틀렸습니다.");
            return mv;
        }

        log.info(signIn.getUserId() + " 회원이 로그인 하였습니다.");
        SessionUtil.setUserInfo(session, user);
        mv.setViewName("/main");

        return mv;
    }
}
