package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.ApiRequestDto;
import com.cafe24.apps.ita.dto.UserDto;
import com.cafe24.apps.ita.entity.ApiRequest;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Mall;
import com.cafe24.apps.ita.repository.ApiRepository;
import com.cafe24.apps.ita.repository.MallRepository;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final ApiRepository apiRepository;
    private final MallRepository mallRepository;

    public ApiService(RestTemplate restTemplate, ApiRepository apiRepository, MallRepository mallRepository) {
        this.restTemplate = restTemplate;
        this.apiRepository = apiRepository;
        this.mallRepository = mallRepository;
    }

    /**
     * Call api
     *
     * @param apiRequestDto
     * @param accessToken
     * @return
     */
    public ResponseEntity<HashMap> callApi(ApiRequestDto apiRequestDto, AccessTokenDto accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Cafe24-Api-Version", apiRequestDto.getVersion());

        HttpEntity<String> entity = new HttpEntity<>(apiRequestDto.getRequestBody(), headers);
        ResponseEntity<HashMap> response = null;

        try {
            String apiUrl = String.format("https://%s.cafe24api.com%s", apiRequestDto.getMallId(), apiRequestDto.getPath());
            response = restTemplate.exchange(apiUrl, Objects.requireNonNull(HttpMethod.resolve(apiRequestDto.getMethod())), entity, HashMap.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * api request 저장
     *
     * @param apiRequest
     */
    public void saveApiRequest(ApiRequest apiRequest) {
        apiRepository.save(apiRequest);
    }

    /**
     * api 리스트 조회
     *
     * @param pageable
     * @param clientIds
     * @return
     */
    public List<ApiRequestDto> getApis(Pageable pageable, List<String> clientIds) {
        List<ApiRequest> apiRequests = apiRepository.findAllByClientIdIn(pageable, clientIds);
        return apiRequests.stream().map(ApiRequest::convertDto).collect(Collectors.toList());
    }

    /**
     * api 단건 조회
     *
     * @param apiIdx
     * @return
     */
    public ApiRequest getApi(Long apiIdx) {
        return apiRepository.getOne(apiIdx);
    }

    /**
     * api 단건 삭제
     *
     * @param apiRequest
     */
    public void deleteApi(ApiRequest apiRequest) {
        apiRepository.delete(apiRequest);
    }

    /**
     * api 전체 삭제
     *
     * @param clientIds
     */
    public void deleteApis(List<String> clientIds) {
        apiRepository.deleteByClientIdIn(clientIds);
    }

    /**
     * apiValid
     *
     * @param app
     * @param apiRequestDto
     * @param session
     * @return
     */
    public boolean apiValid(App app, ApiRequestDto apiRequestDto, HttpSession session) {
        String operationLevel = app.convertDto().getOperationLevel();
        UserDto userDto = SessionUtil.getUserInfo(session);

        //요청앱의 운영레벨과 유저의 운영레벨이 같은지 확인
        boolean result = userDto.getOperationLevel().contains(operationLevel);
        if (!result) {
            return false;
        }

        //몰의 운영레벨 확인
        List<Mall> malls = mallRepository.findAllByOperationLevel(operationLevel);
        for (Mall mall : malls) {
            if (mall.getMallId().equals(apiRequestDto.getMallId())){
                return true;
            }
        }

        return false;
    }
}
