package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.CodeDto;
import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.repository.AccessTokenRepository;
import com.cafe24.apps.ita.repository.AppRepository;
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
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private static final String CAFE24_API_BASE_URL = "https://{{mall_id}}.cafe24api.com";
    private static final String API_OAUTH_TOKEN = CAFE24_API_BASE_URL + "/api/v2/oauth/token";

    private final AppRepository appRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RestTemplate restTemplate;

    public AuthService(AppRepository appRepository, AccessTokenRepository accessTokenRepository, RestTemplate restTemplate) {
        this.appRepository = appRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * hmac 검증 : +-1분내에 생성된 요청만 처리
     *
     * @param mallDto
     * @return
     */
    public boolean checkTimestamp(MallDto mallDto) {
        LocalDateTime afterOne = LocalDateTime.now().plusMinutes(1L);
        LocalDateTime beforeOne = LocalDateTime.now().minusMinutes(1L);
        LocalDateTime timestamp = new Timestamp(mallDto.getTimestamp() * 1000).toLocalDateTime();

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
        String hmac = EncryptUtil.makeHmac(queryArr[0], app.get().getSecretKey());

        return requestHmac.equals(hmac);
    }

    /**
     * Refresh Token 만료확인
     * 만료 : true
     *
     * @param app
     * @param mallDto
     * @return
     */
    public boolean isExpireRefreshToken(App app, MallDto mallDto) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByAppAndMallId(app, mallDto.getMall_id());
        //토큰이 없으면
        if (accessToken.isEmpty()) {
            return true;
        }

        //리프레시토큰 만료
        return accessToken.get().isRefreshTokenExpire();
    }

    /**
     * 코드 발급 url
     *
     * @param app
     * @param mallDto
     * @param request
     * @return
     */
    public String getCodeRedirectUrl(App app, MallDto mallDto, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString().replace("http://", "https://");

        return "redirect:" +
                "https://" + mallDto.getMall_id() + ".cafe24api.com" +
                "/api/v2/oauth/authorize" +
                "?response_type=" + "code" +
                "&client_id=" + app.getClientId() +
                "&state=" + request.getRequestedSessionId() +
                "&redirect_uri=" + requestUrl + "/code" +
                "&scope=" + String.join(",", app.getScopes());
    }

    /**
     * Access token
     *
     * @param appIdx
     * @param codeDto
     * @param session
     * @param request
     * @return
     */
    public AccessToken getAccessToken(Long appIdx, CodeDto codeDto, HttpSession session, HttpServletRequest request) throws Exception {
        if (!session.getId().equals(codeDto.getState())) {
            throw new Exception("state 변조 경고");
        }

        Optional<App> appOptional = appRepository.findById(appIdx);
        appOptional.orElseThrow(() -> new Exception("app 정보 없음"));
        App app = appOptional.get();

        MallDto mallInfo = SessionUtil.getMallInfo(session);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, app.getAuthorization());
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String callbackUrl = "https://" + request.getServerName() + request.getContextPath() + "/auth/" + app.getIdx() + "/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", codeDto.getCode());
        requestBody.add("redirect_uri", callbackUrl);

        String url = API_OAUTH_TOKEN.replace("{{mall_id}}", mallInfo.getMall_id());

        AccessTokenDto accessTokenDto = restTemplate.postForObject(url, new HttpEntity<>(requestBody, header), AccessTokenDto.class);
        if (accessTokenDto == null) {
            throw new Exception("Access Token 조회에 실패했습니다.");
        }
        System.out.println(accessTokenDto.toString());
        return accessTokenDto.toEntity();
    }
}
