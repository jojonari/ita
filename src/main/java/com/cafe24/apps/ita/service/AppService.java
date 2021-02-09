package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.repository.AppRepository;
import org.springframework.stereotype.Service;

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
    public App saveApp(App app) {
        return appRepository.save(app);
    }

    /**
     * App 조회
     * @param clientId
     * @return
     */
    public App getApp(String clientId) {
        return appRepository.findByClientId(clientId);
    }
}
