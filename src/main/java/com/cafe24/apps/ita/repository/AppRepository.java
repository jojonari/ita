package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface AppRepository extends JpaRepository<App, Long> {
    App findByClientId(String clientId);

    App findByIdxAndUser(Long idx, User user);

    List<App> findAllByUserAndClientIdContainingOrderByIdxDesc(Pageable pageable, User user, String clientId);

    App findByUserAndClientId(User user, String clientId);
}
