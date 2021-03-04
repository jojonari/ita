package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface WebhookRepository extends JpaRepository<Webhook, Long> {

    List<Webhook> findAllByClientIdIn(List<String> clientId);

    void deleteByClientIdIn(List<String> clientIds);
}
