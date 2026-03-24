package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentScale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentScaleRepository extends JpaRepository<AssessmentScale, Long> {

  List<AssessmentScale> findByStatusOrderByIdAsc(Integer status);

  Optional<AssessmentScale> findByScaleCode(String scaleCode);

  List<AssessmentScale> findByDeletedFlagOrderByIdDesc(Integer deletedFlag);
}
