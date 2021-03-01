package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppService {
    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
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
        return appRepository.findByClientId(clientId);
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
    public App getApp(HttpSession session, Long appIdx) {
        User user = SessionUtil.getUserInfo(session);
        return appRepository.findByIdxAndUser(appIdx, user);
    }

    /**
     * App 목록 조회
     *
     * @param session
     * @param clientId
     * @return
     */
    public List<AppDto> getApps(HttpSession session, Optional<String> clientId) {
        User user = SessionUtil.getUserInfo(session);
        List<App> apps = appRepository.findAllByUserAndClientIdContainingOrderByIdxDesc(user, clientId.orElse(""));

        return apps.stream().map(App::convertDto).collect(Collectors.toList());

    }

    /**
     * 앱 수정
     *
     * @param app
     * @return
     */
    public App modifyApp(App app) {
        return appRepository.saveAndFlush(app);
    }

    /**
     * 앱 삭제
     *
     * @param appIdx
     */
    public void deleteApp(Long appIdx) {
        appRepository.deleteById(appIdx);
    }

    /**
     * app client list 조회
     * @param session
     * @param clientId
     * @return
     */
    public List<String> getAppClientIds(HttpSession session, Optional<String> clientId) {
        List<AppDto> appDtos = this.getApps(session, clientId);

        return appDtos.stream().map(AppDto::getClientId).collect(Collectors.toList());
    }
}
