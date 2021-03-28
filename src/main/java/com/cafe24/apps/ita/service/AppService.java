package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.PrivateAppDto;
import com.cafe24.apps.ita.dto.UserDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.repository.ApiRepository;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.repository.WebhookRepository;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppService {
    private final AppRepository appRepository;
    private final WebhookRepository webhookRepository;
    private final ApiRepository apiRepository;

    public AppService(AppRepository appRepository, WebhookRepository webhookRepository, ApiRepository apiRepository) {
        this.appRepository = appRepository;
        this.webhookRepository = webhookRepository;
        this.apiRepository = apiRepository;
    }

    /**
     * 앱 등록
     *
     * @param app
     * @return
     */
    public App registerApp(App app) {
        return appRepository.save(app);
    }

    /**
     * App 단건 조회
     *
     * @param clientId
     * @return
     */
    public App getApp(String clientId) {
        Optional<App> optionalApp = appRepository.findByClientId(clientId);

        return optionalApp.isEmpty() ? null : optionalApp.get();
    }

    /**
     * App 단건 조회
     *
     * @param appIdx
     * @return
     */
    public App getApp(long appIdx) throws Exception {
        Optional<App> appOptional = appRepository.findById(appIdx);
        return appOptional.orElseThrow(() -> new Exception("App 정보 없음"));
    }

    /**
     * App 단건 조회
     *
     * @param session
     * @param appIdx
     * @return
     */
    public PrivateAppDto getApp(HttpSession session, Long appIdx) {
        User user = SessionUtil.getUserInfo(session).toEntity();
        Optional<App> app = appRepository.findByIdxAndUser(appIdx, user);

        return app.isEmpty() ? null : app.get().convertPrivateDto();
    }

    /**
     * App 단건 조회
     *
     * @param session
     * @param clientId
     * @return
     */
    public PrivateAppDto getApp(HttpSession session, String clientId) {
        User user = SessionUtil.getUserInfo(session).toEntity();
        Optional<App> optionalApp = appRepository.findByUserAndClientId(user, clientId);
        return optionalApp.isEmpty() ? null : optionalApp.get().convertPrivateDto();
    }

    /**
     * App 목록 조회
     *
     * @param session
     * @param pageable
     * @param clientId
     * @return
     */
    public List<AppDto> getApps(HttpSession session, Pageable pageable, Optional<String> clientId) {
        User user = SessionUtil.getUserInfo(session).toEntity();
        List<App> apps = appRepository.findAllByUserAndClientIdContainingOrderByIdxDesc(pageable, user, clientId.orElse(""));

        return apps.stream().map(App::convertDto).collect(Collectors.toList());

    }

    /**
     * 앱 수정
     *
     * @param app
     * @return
     */
    public AppDto modifyApp(App app) {
        App saveApp = appRepository.saveAndFlush(app);
        return saveApp.convertDto();
    }

    /**
     * 앱 삭제
     *
     * @param app
     */
    public void deleteApp(App app) {
        String clientId = app.convertPrivateDto().getClientId();

        apiRepository.deleteByClientId(clientId);
        webhookRepository.deleteByClientId(clientId);
        appRepository.delete(app);
    }

    /**
     * app client list 조회
     *
     * @param session
     * @param clientId
     * @return
     */
    public List<String> getAppClientIds(HttpSession session, Optional<String> clientId) {
        List<AppDto> appDtos = this.getApps(session, null, clientId);

        return appDtos.stream().map(AppDto::getClientId).collect(Collectors.toList());
    }

    /**
     * valid
     *
     * @param app
     */
    public boolean appValid(PrivateAppDto app) {
        UserDto userDto = app.getUser().convertDto();

        return userDto.getGrantType().contains(app.getGrantType()) && userDto.getOperationLevel().contains(app.getOperationLevel());
    }
}
