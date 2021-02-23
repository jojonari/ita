package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.MallDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.util.EncryptUtil;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final AppRepository appRepository;

    public AuthService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * hmac 검증 : +-1분내에 생성된 요청만 처리
     *
     * @param mallDto
     * @return
     */
    public boolean checkTimestamp(MallDto mallDto) {
        LocalDateTime after = LocalDateTime.now().plusMinutes(1L);
        LocalDateTime before = LocalDateTime.now().minusMinutes(1L);
        LocalDateTime timestamp = new Timestamp(mallDto.getTimestamp()).toLocalDateTime();

        return timestamp.isBefore(after) && timestamp.isAfter(before);
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


}
