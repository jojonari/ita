package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.*;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Mall;
import com.cafe24.apps.ita.repository.AccessTokenRepository;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.repository.MallRepository;
import com.cafe24.apps.ita.util.EncryptUtil;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final String CAFE24_API_BASE_URL = "https://{{mall_id}}.cafe24api.com";
    private static final String API_OAUTH_TOKEN = CAFE24_API_BASE_URL + "/api/v2/oauth/token";

    private final AppRepository appRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RestTemplate restTemplate;
    private final MallRepository mallRepository;

    public AuthService(AppRepository appRepository, AccessTokenRepository accessTokenRepository, RestTemplate restTemplate, MallRepository mallRepository) {
        this.appRepository = appRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.restTemplate = restTemplate;
        this.mallRepository = mallRepository;
    }

    /**
     * hmac 검증 : +-1분내에 생성된 요청만 처리
     *
     * @param authMallDto
     * @return
     */
    public boolean checkTimestamp(AuthMallDto authMallDto) {
        LocalDateTime afterOne = LocalDateTime.now().plusMinutes(1L);
        LocalDateTime beforeOne = LocalDateTime.now().minusMinutes(1L);
        LocalDateTime timestamp = new Timestamp(authMallDto.getTimestamp() * 1000).toLocalDateTime();

        return timestamp.isBefore(afterOne) && timestamp.isAfter(beforeOne);
    }

    /**
     * hmac 검증
     *
     * @param appId
     * @param queryString
     * @return
     * @throws Exception
     */
    public boolean checkHmac(Long appId, String queryString) throws Exception {
        Optional<App> app = appRepository.findById(appId);
        app.orElseThrow(() -> new Exception("app 정보가 없습니다."));

        String[] queryArr = queryString.split("&hmac=");
        String requestHmac = URLDecoder.decode(queryArr[1], StandardCharsets.UTF_8);
        String hmac = EncryptUtil.makeHmac(queryArr[0], app.get().convertPrivateDto().getSecretKey());

        return requestHmac.equals(hmac);
    }

    /**
     * Refresh Token 만료확인
     * 만료 : true
     *
     * @param privateAppDto
     * @param mallId
     * @return
     */
    public boolean isExpireRefreshToken(PrivateAppDto privateAppDto, String mallId) {
        //client credentials 일 때
        if (!privateAppDto.isAuthorizationCode()) {
            Optional<AccessToken> accessToken = accessTokenRepository.findByApp(privateAppDto.toEntity());
            //토큰이 없으면
            if (accessToken.isEmpty()) {
                return true;
            }

            return accessToken.get().convertDto().isAccessTokenExpire();
        }

        //authorization_code 일 때
        Optional<AccessToken> accessToken = accessTokenRepository.findByAppAndMallId(privateAppDto.toEntity(), mallId);
        //토큰이 없으면
        if (accessToken.isEmpty()) {
            return true;
        }

        //리프레시토큰 만료
        return accessToken.get().convertDto().isRefreshTokenExpire();
    }

    /**
     * 코드 발급 url
     *
     * @param privateAppDto
     * @param authMallDto
     * @param request
     * @return
     */
    public String getCodeRedirectUrl(PrivateAppDto privateAppDto, AuthMallDto authMallDto, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString().replace("http://", "https://");

        return "redirect:" +
                "https://" + authMallDto.getMall_id() + ".cafe24api.com" +
                "/api/v2/oauth/authorize" +
                "?response_type=" + "code" +
                "&client_id=" + privateAppDto.getClientId() +
                "&state=" + request.getRequestedSessionId() +
                "&redirect_uri=" + requestUrl + "/redirect" +
                "&scope=" + String.join(",", privateAppDto.getScopes());
    }

    /**
     * Access token
     *
     * @param privateAppDto
     * @param codeDto
     * @param request
     * @return
     */
    public AccessToken getAccessToken(PrivateAppDto privateAppDto, CodeDto codeDto, HttpServletRequest request) throws Exception {
        AuthMallDto mallInfo = SessionUtil.getMallInfo(request.getSession());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, privateAppDto.getAuthorization());
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String callbackUrl = "https://" + request.getServerName() + request.getContextPath() + "/auth/" + privateAppDto.getIdx() + "/redirect";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", codeDto.getCode());
        requestBody.add("redirect_uri", callbackUrl);

        String url = API_OAUTH_TOKEN.replace("{{mall_id}}", mallInfo.getMall_id());

        AccessTokenDto accessTokenDto = restTemplate.postForObject(url, new HttpEntity<>(requestBody, header), AccessTokenDto.class);
        if (accessTokenDto == null) {
            throw new Exception("Access Token 발급에 실패했습니다.");
        }

        AccessToken accessToken = accessTokenDto.toEntity(privateAppDto.toEntity());
        this.saveAccessToken(accessToken);

        return accessToken;
    }

    /**
     * AccessToken 저장
     *
     * @param accessToken
     * @return
     */
    public void saveAccessToken(AccessToken accessToken) {
        AccessTokenDto accessTokenDto = accessToken.convertDto();

        accessTokenRepository.deleteByClientIdAndMallId(accessTokenDto.getClient_id(), accessTokenDto.getMall_id());
        accessTokenRepository.save(accessToken);
    }

    /**
     * AccessToken CC 저장
     *
     * @param accessToken
     * @return
     */
    public void saveAccessTokenOfClientCredentials(AccessToken accessToken) {
        accessTokenRepository.deleteByClientId(accessToken.convertApiDto().getClient_id());
        accessTokenRepository.save(accessToken);
    }

    /**
     * AccessToken 조회
     *
     * @param privateAppDto
     * @param mallId
     * @return
     */
    public AccessTokenDto getAccessToken(PrivateAppDto privateAppDto, String mallId) throws Exception {
        Optional<AccessToken> accessTokenOptional;
        if (privateAppDto.isAuthorizationCode()) {
            accessTokenOptional = accessTokenRepository.findByAppAndMallId(privateAppDto.toEntity(), mallId);
        } else {
            accessTokenOptional = accessTokenRepository.findByApp(privateAppDto.toEntity());
        }

        AccessToken accessToken = accessTokenOptional.orElseThrow(() -> new Exception("access token이 없습니다."));
        AccessTokenDto accessTokenApiDto = accessToken.convertApiDto();
        if (!accessTokenApiDto.isAccessTokenExpire()) {
            return accessTokenApiDto;
        }

        if (privateAppDto.isClientCredentials()) {
            return this.getAccessTokenByClientCredential(privateAppDto, mallId);
        }

        if (accessTokenApiDto.isRefreshTokenExpire()) {
            throw new Exception("refresh token이 만료되어 API를 호출 할 수 없습니다.");
        }

        return this.getAccessTokenWithRefreshToken(privateAppDto, accessTokenApiDto);
    }

    /**
     * 리프레시토큰을 사용하여 토큰 조회
     *
     * @param privateAppDto
     * @param accessToken
     */
    private AccessTokenDto getAccessTokenWithRefreshToken(PrivateAppDto privateAppDto, AccessTokenDto accessToken) throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, privateAppDto.getAuthorization());
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", accessToken.getRefresh_token());

        String url = API_OAUTH_TOKEN.replace("{{mall_id}}", accessToken.getMall_id());

        AccessTokenDto accessTokenDto = restTemplate.postForObject(url, new HttpEntity<>(requestBody, header), AccessTokenDto.class);
        if (accessTokenDto == null) {
            throw new Exception("Access Token 발급에 실패했습니다.");
        }

        AccessToken accessTokenRefresh = accessTokenDto.toEntity(privateAppDto.toEntity());
        this.saveAccessToken(accessTokenRefresh);

        return accessTokenDto;
    }

    /**
     * Api 호출용 mallIds의 TextValue 조회
     *
     * @param privateAppDto
     * @return
     */
    public List<TextValue> getTextValuesSetMallId(PrivateAppDto privateAppDto) {
        List<AccessToken> accessTokens = accessTokenRepository.findAllByApp(privateAppDto.toEntity());

        return accessTokens.stream().map(AccessToken::convertDto).map(AccessTokenDto::convertTextValueSetMallId).collect(Collectors.toList());
    }

    /**
     * Api 호출용 mallIds의 TextValue 조회
     *
     * @param privateAppDto
     * @return
     */
    public List<TextValue> getTextValuesSetMallIdByOperationLevel(PrivateAppDto privateAppDto) {
        List<Mall> malls = mallRepository.findAllByOperationLevel(privateAppDto.getOperationLevel());

        return malls.stream().map(Mall::convertDto).map(m -> new TextValue(m.getMallId(), m.getMallId())).collect(Collectors.toList());
    }

    /**
     * 클라이언트 크리덴셜 앱의 토큰 발급
     *
     * @param privateAppDto
     * @param mallId
     * @return
     */
    public AccessTokenDto getAccessTokenByClientCredential(PrivateAppDto privateAppDto, String mallId) throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, privateAppDto.getAuthorization());
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        String url = API_OAUTH_TOKEN.replace("{{mall_id}}", mallId);

        AccessTokenDto accessTokenDto = restTemplate.postForObject(url, new HttpEntity<>(requestBody, header), AccessTokenDto.class);
        if (accessTokenDto == null) {
            throw new Exception("Access Token 발급에 실패했습니다.");
        }

        AccessToken accessToken = accessTokenDto.toClientCredentialsEntity(privateAppDto.toEntity());
        this.saveAccessTokenOfClientCredentials(accessToken);
        return accessToken.convertApiDto();
    }
}
