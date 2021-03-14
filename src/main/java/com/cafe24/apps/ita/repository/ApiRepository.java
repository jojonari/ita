package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.ApiRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ApiRepository extends JpaRepository<ApiRequest, Long> {

    List<ApiRequest> findAllByClientIdIn(Pageable pageable, List<String> clientId);

    void deleteByClientIdIn(List<String> clientIds);
}
