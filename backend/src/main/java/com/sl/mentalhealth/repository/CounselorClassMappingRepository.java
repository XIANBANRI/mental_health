package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.CounselorClassMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselorClassMappingRepository
    extends JpaRepository<CounselorClassMapping, Long> {

  Optional<CounselorClassMapping> findFirstByClassName(String className);
}