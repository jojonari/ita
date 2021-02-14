package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.entity.SignIn;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원 가입
     *
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     */
    public User regisertUser(User user) throws NoSuchAlgorithmException {
        user.encryptPassword();
        return userRepository.save(user);
    }

    /**
     * 로그인 시도
     *
     * @param signIn
     * @param user
     * @return
     * @throws Exception
     */
    public boolean doLogin(SignIn signIn, User user) throws NoSuchAlgorithmException {
        return signIn.isEqualsPassWord(user);
    }

    /**
     * 회원정보 조회
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Optional<User> getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}
