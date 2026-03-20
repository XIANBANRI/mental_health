package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Counselor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselorRepository extends JpaRepository<Counselor, String> {

  Optional<Counselor> findFirstByCollegeAndGrade(String college, String grade);
}