package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.ApiRequestDto;
import com.cafe24.apps.ita.entity.ApiRequest;
import com.cafe24.apps.ita.repository.ApiRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final ApiRepository apiRepository;

    public ApiService(RestTemplate restTemplate, ApiRepository apiRepository) {
        this.restTemplate = restTemplate;
        this.apiRepository = apiRepository;
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
}
