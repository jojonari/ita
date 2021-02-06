package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.SignIn;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.service.UserService;
import com.cafe24.apps.ita.util.SessionUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Log4j2
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
    @PostMapping(value = "/sign-up")
    public ModelAndView signUp(HttpSession session, User user, ModelAndView mv) throws NoSuchAlgorithmException {
        Optional<User> userInfo = userService.getUser(user.getUserId());
        if (userInfo.isPresent()) {
            user.deleteUserPw();
            mv.addObject("user", user);
            mv.setViewName("/user/sign-up");
            mv.addObject("error_msg", "이미 사용중인 ID 입니다.");
            return mv;
        }

        User regisertUser = userService.regisertUser(user);
        SessionUtil.setUserInfo(session, regisertUser);

        mv.setViewName("redirect:/main");
        return mv;
    }

    /**
     * 회원 로그인 화면
     *
     * @param session
     * @return
     */
    @GetMapping("/sign-in")
    public ModelAndView signIn(HttpSession session, ModelAndView mv) {
        if (SessionUtil.isSignIn(session)) {
            mv.setViewName("redirect:/main");
        }

        mv.setViewName("/user/sign-in");
        return mv;
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
    @PostMapping(value = "/sign-in")
    public ModelAndView doLogin(HttpSession session, SignIn signIn, ModelAndView mv) throws Exception {
        Optional<User> optionalUser = userService.getUser(signIn.getUserId());
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

        mv.setViewName("redirect:/main");
        return mv;
    }
}
