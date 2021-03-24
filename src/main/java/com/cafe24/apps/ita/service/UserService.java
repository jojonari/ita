package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.PrivateUserDto;
import com.cafe24.apps.ita.dto.SignIn;
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
     * @param privateUserDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    public PrivateUserDto regisertUser(PrivateUserDto privateUserDto) throws NoSuchAlgorithmException {
        privateUserDto.encryptPassword();
        privateUserDto.defaultUserSet();
        User saveUser = userRepository.save(privateUserDto.toEntity());
        return saveUser.convertPrivateDto();
    }

    /**
     * 로그인 시도
     *
     * @param signIn
     * @param privateUserDto
     * @return
     * @throws Exception
     */
    public boolean doLogin(SignIn signIn, PrivateUserDto privateUserDto) throws NoSuchAlgorithmException {
        return signIn.isEqualsPassWord(privateUserDto);
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
