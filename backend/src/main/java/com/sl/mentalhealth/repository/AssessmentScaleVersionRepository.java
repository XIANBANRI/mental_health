package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentScaleVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentScaleVersionRepository extends JpaRepository<AssessmentScaleVersion, Long> {

  List<AssessmentScaleVersion> findByScaleIdOrderByVersionNoDesc(Long scaleId);

  Optional<AssessmentScaleVersion> findFirstByScaleIdOrderByVersionNoDesc(Long scaleId);
}