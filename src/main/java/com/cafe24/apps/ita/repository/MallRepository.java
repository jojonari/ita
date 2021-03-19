package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.Mall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MallRepository extends JpaRepository<Mall, Long> {

    List<Mall> findAllByOperationLevel(String operationLevel);
}