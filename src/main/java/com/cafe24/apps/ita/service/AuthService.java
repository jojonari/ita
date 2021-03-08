package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.AppDto;
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
        String hmac = EncryptUtil.makeHmac(queryArr[0], app.get().convertPrivateDto().getSecretKey());

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
     * @param appDto
     * @param mallDto
     * @param request
     * @return
     */
    public String getCodeRedirectUrl(AppDto appDto, MallDto mallDto, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString().replace("http://", "https://");

        return "redirect:" +
                "https://" + mallDto.getMall_id() + ".cafe24api.com" +
                "/api/v2/oauth/authorize" +
                "?response_type=" + "code" +
                "&client_id=" + appDto.getClientId() +
                "&state=" + request.getRequestedSessionId() +
                "&redirect_uri=" + requestUrl + "/redirect" +
                "&scope=" + String.join(",", appDto.getScopes());
    }

    /**
     * Access token
     *
     * @param app
     * @param codeDto
     * @param request
     * @return
     */
    public AccessToken getAccessToken(App app, CodeDto codeDto, HttpServletRequest request) throws Exception {
        MallDto mallInfo = SessionUtil.getMallInfo(request.getSession());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, app.getAuthorization());
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String callbackUrl = "https://" + request.getServerName() + request.getContextPath() + "/auth/" + app.convertDto().getIdx() + "/redirect";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", codeDto.getCode());
        requestBody.add("redirect_uri", callbackUrl);

        String url = API_OAUTH_TOKEN.replace("{{mall_id}}", mallInfo.getMall_id());

        AccessTokenDto accessTokenDto = restTemplate.postForObject(url, new HttpEntity<>(requestBody, header), AccessTokenDto.class);
        if (accessTokenDto == null) {
            throw new Exception("Access Token 발급에 실패했습니다.");
        }

        AccessToken accessToken = accessTokenDto.toEntity();
        accessToken.setApp(app);

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
     * AccessToken 조회
     *
     * @param app
     * @param mallId
     * @return
     */
    public AccessToken getAccessToken(App app, String mallId) throws Exception {
        Optional<AccessToken> accessTokenOptional = accessTokenRepository.findByAppAndMallId(app, mallId);

        AccessToken accessToken = accessTokenOptional.orElseThrow(() -> new Exception("access token이 없습니다."));
        if (!accessToken.isAccessTokenExpire()) {
            return accessToken;
        }

        if (accessToken.isRefreshTokenExpire()){
            throw new Exception("refresh_token이 만료되어 API를 호출 할 수 없습니다.");
        }

        System.out.println("리프레시토큰을 사용해야합니다");
        return null;
    }
}
