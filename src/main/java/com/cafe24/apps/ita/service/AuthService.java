package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.repository.AccessTokenRepository;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.util.EncryptUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final AppRepository appRepository;
    private final AccessTokenRepository accessTokenRepository;

    public AuthService(AppRepository appRepository, AccessTokenRepository accessTokenRepository) {
        this.appRepository = appRepository;
        this.accessTokenRepository = accessTokenRepository;
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
        LocalDateTime timestamp = new Timestamp(mallDto.getTimestamp()).toLocalDateTime();

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
        return "redirect:" +
                "https://" + mallDto.getMall_id() + ".cafe24api.com" +
                "/api/v2/oauth/authorize" +
                "?response_type=" + "code" +
                "&client_id=" + app.getClientId() +
                "&state=" + request.getSession().getId() +
                "&redirect_uri=" + request.getRequestURL() + "/auth/" + app.getIdx() + "/code" +
                "&scope=" + app.getScopes();
    }
}
