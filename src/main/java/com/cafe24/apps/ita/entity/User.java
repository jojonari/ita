package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.PrivateAppDto;
import com.cafe24.apps.ita.dto.PrivateUserDto;
import com.cafe24.apps.ita.dto.UserDto;
import com.cafe24.apps.ita.util.EncryptUtil;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.security.NoSuchAlgorithmException;

@Entity
@ToString(exclude = "userPw")
@Table(name = "t_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Length(min = 4, message = "*Your ID must have at least 5 characters")
    @NotEmpty(message = "*Please provide an groupware_id")
    @Column(length = 30, nullable = false, unique = true)
    private String userId;

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    @Column(length = 128, nullable = false)
    private String userPw;

    @NotEmpty(message = "*Please provide your name")
    @Column(length = 30, nullable = false)
    private String userName;

    @NotEmpty(message = "*Please provide your Team name")
    @Column(length = 30, nullable = false)
    private String teamName;

    @Builder
    public User(Long idx, String userId, String userPw, String userName, String teamName) {
        this.idx = idx;
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.teamName = teamName;
    }

    /**
     * convert DTO
     *
     * @return
     */
    public PrivateUserDto convertPrivateDto() {
        return PrivateUserDto.builder().
                idx(this.idx)
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .teamName(this.teamName)
                .build();
    }
    /**
     * convert DTO
     *
     * @return
     */
    public UserDto convertDto() {
        return UserDto.builder().
                idx(this.idx)
                .userId(this.userId)
                .userName(this.userName)
                .teamName(this.teamName)
                .build();
    }

    /**
     * 패스워드 암호화
     *
     * @throws NoSuchAlgorithmException
     */
    public void encryptPassword() throws NoSuchAlgorithmException {
        this.userPw = EncryptUtil.encryptPassword(this.userPw);
    }

    public String getUserId() {
        return this.userId;
    }

    public boolean isEqualsPassWord(String encUserPw) {
        return this.userPw.equals(encUserPw);
    }
}
