package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Counselor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, String>,
    JpaSpecificationExecutor<Counselor> {

  Optional<Counselor> findByAccount(String account);

  boolean existsByAccount(String account);
}