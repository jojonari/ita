package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.ApiRequestDto;
import com.cafe24.apps.ita.util.JsonUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiRequest extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 30, nullable = false)
    private String clientId;

    @Column(length = 16, nullable = false)
    private String mallId;

    @Column(length = 10, nullable = false)
    private String method;

    @Column(length = 20)
    private String version;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String apiUrl;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Builder
    public ApiRequest(Long idx, String clientId, String mallId, String method, String version, String apiUrl, String requestBody, String response) {
        this.idx = idx;
        this.clientId = clientId;
        this.mallId = mallId;
        this.method = method;
        this.version = version;
        this.apiUrl = apiUrl;
        this.requestBody = requestBody;
        this.response = response;
    }

    public ApiRequest(ApiRequestDto api, ResponseEntity<HashMap> response) {
        this.clientId = api.getClientId();
        this.mallId = api.getMallId();
        this.method = api.getMethod();
        this.version = api.getVersion();
        this.apiUrl = api.getApiUrl();
        this.requestBody = api.getRequestBody();
        this.response = JsonUtil.toJSON(response);
    }

    /**
     * convert DTO
     *
     * @return
     */
    public ApiRequestDto convertDto() {
        return ApiRequestDto.builder().
                idx(this.idx)
                .clientId(this.clientId)
                .mallId(this.mallId)
                .method(this.method)
                .version(this.version)
                .apiUrl(this.apiUrl)
                .requestBody(this.requestBody)
                .response(JsonUtil.convertPrettyJson(this.response))
                .createdDate(this.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedDate(this.modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
