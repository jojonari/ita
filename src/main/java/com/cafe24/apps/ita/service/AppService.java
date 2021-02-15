package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.repository.AppRepository;
import com.cafe24.apps.ita.util.SessionUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
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
    public App saveApp(App app) {
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
     * App 목록 조회
     * @param session
     * @return
     */
    public List<AppDto> getApps(HttpSession session) {
        User user = SessionUtil.getUserInfo(session);
        List<App> apps = appRepository.findAllByUser(user);

        return apps.stream().map(App::convertDto).collect(Collectors.toList());

    }
}
