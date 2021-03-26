package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByClientId(String clientId);

    Optional<App> findByIdxAndUser(Long idx, User user);

    List<App> findAllByUserAndClientIdContainingOrderByIdxDesc(Pageable pageable, User user, String clientId);

    Optional<App> findByUserAndClientId(User user, String clientId);
}
