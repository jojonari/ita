package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByAppAndMallId(App app, String mallId);

    Optional<AccessToken> findByApp(App app);

    void deleteByClientIdAndMallId(String clientId, String mallId);

    List<AccessToken> findAllByApp(App app);

    void deleteByClientId(String clientId);
}
