package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentScale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentScaleRepository extends JpaRepository<AssessmentScale, Long> {

  List<AssessmentScale> findByStatusOrderByIdAsc(Integer status);
}
