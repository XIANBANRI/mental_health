package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Counselor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, String> {

  Optional<Counselor> findByAccount(String account);
}