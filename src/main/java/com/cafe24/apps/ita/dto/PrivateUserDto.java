package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.util.EncryptUtil;
import com.cafe24.apps.ita.util.JsonUtil;
import lombok.Builder;
import lombok.Data;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class PrivateUserDto {

    private Long idx;
    private String userId;
    private String userPw;
    private String userName;
    private String teamName;
    private Set<String> operationLevel;
    private Set<String> grantType;

    @Builder
    public PrivateUserDto(Long idx, String userId, String userPw, String userName, String teamName, Set<String> operationLevel, Set<String> grantType) {
        this.idx = idx;
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.teamName = teamName;
        this.operationLevel = operationLevel;
        this.grantType = grantType;
    }

    /**
     * toEntity
     *
     * @return
     */
    public User toEntity() {
        return User.builder()
                .idx(idx)
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .teamName(teamName)
                .operationLevel(operationLevel)
                .grantType(grantType)
                .build();
    }

    /**
     * 패스워드 같은지 비교
     *
     * @param encUserPw
     * @return
     */
    public boolean isEqualsPassWord(String encUserPw) {
        return this.userPw.equals(encUserPw);
    }

    /**
     * operationLevelTextValue
     *
     * @return
     */
    public String operationLevelTextValue() {
        List<TextValue> textValues = this.operationLevel.stream().map(x -> new TextValue(x.toUpperCase(), x.toLowerCase())).collect(Collectors.toList());
        return JsonUtil.toJSON(textValues);
    }

    /**
     * operationLevelTextValue
     *
     * @return
     */
    public String grantTypeTextValue() {
        List<TextValue> textValues = this.grantType.stream().map(x -> new TextValue(x.toUpperCase(), x.toLowerCase())).collect(Collectors.toList());
        return JsonUtil.toJSON(textValues);
    }

    /**
     * 기본 권한 설정
     */
    public void defaultUserSet() {
        //기본 권한 설정
        this.operationLevel = Set.of("dev", "qa");
        //기본 인증방식 설정
        this.grantType = Set.of("authorization_code");
    }

    /**
     * 패스워드 암호화
     *
     * @throws NoSuchAlgorithmException
     */
    public void encryptPassword() throws NoSuchAlgorithmException {
        this.userPw = EncryptUtil.encryptPassword(this.userPw);
    }

}
