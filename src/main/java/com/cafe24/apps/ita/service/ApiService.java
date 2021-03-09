package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.ApiDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void callApi(ApiDto apiDto, AccessTokenDto accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Cafe24-Api-Version", apiDto.getVersion());

        HttpEntity<String> entity = new HttpEntity<>(apiDto.getRequestBody(), headers);
        ResponseEntity<HashMap> response;
        RestClientException restClientException = null;
        try {
            response = restTemplate.exchange(apiDto.getUrl(), Objects.requireNonNull(HttpMethod.resolve(apiDto.getMethods())), entity, HashMap.class);
        } catch (RestClientException e) {

        }
    }
}
