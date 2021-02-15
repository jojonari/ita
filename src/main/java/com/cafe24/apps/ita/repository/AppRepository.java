package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AppRepository extends JpaRepository<App, Long> {
    App findByClientId(String clientId);

    List<App> findAllByUser(User user);
}
