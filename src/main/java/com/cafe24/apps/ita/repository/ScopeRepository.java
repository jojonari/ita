package com.cafe24.apps.ita.repository;

import com.cafe24.apps.ita.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ScopeRepository extends JpaRepository<Scope, Long> {

}