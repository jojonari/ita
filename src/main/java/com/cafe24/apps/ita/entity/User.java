package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.PrivateUserDto;
import com.cafe24.apps.ita.dto.UserDto;
import com.cafe24.apps.ita.util.EncryptUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

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

    @CollectionTable(name = "m_user_operationLevel")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 30, nullable = false)
    private Set<String> operationLevel;

    @CollectionTable(name = "m_user_grantType")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 30, nullable = false)
    private Set<String> grantType;

    @Builder
    public User(Long idx, String userId, String userPw, String userName, String teamName, Set<String> operationLevel, Set<String> grantType) {
        this.idx = idx;
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.teamName = teamName;
        this.operationLevel = operationLevel;
        this.grantType = grantType;
    }

    /**
     * convert DTO
     *
     * @return PrivateUserDto
     */
    public PrivateUserDto convertPrivateDto() {
        return PrivateUserDto.builder().
                idx(this.idx)
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .teamName(this.teamName)
                .operationLevel(this.operationLevel)
                .grantType(this.grantType)
                .build();
    }

    /**
     * convert DTO
     *
     * @return UserDto
     */
    public UserDto convertDto() {
        return UserDto.builder().
                idx(this.idx)
                .userId(this.userId)
                .userName(this.userName)
                .teamName(this.teamName)
                .operationLevel(this.operationLevel)
                .grantType(this.grantType)
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

    /**
     * 기본 권한 설정
     */
    public void defaultUserSet(){
        //기본 권한 설정
        this.operationLevel = Set.of("dev", "qa");
        //기본 인증방식 설정
        this.grantType = Set.of("authorization_code");

    }

    /**
     * 패스워드 비교
     * @param encUserPw
     * @return
     */
    public boolean isEqualsPassWord(String encUserPw) {
        return this.userPw.equals(encUserPw);
    }

    public String getUserId() {
        return this.userId;
    }
}
