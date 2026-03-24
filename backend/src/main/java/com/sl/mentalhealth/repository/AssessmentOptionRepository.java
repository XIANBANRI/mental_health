package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentOptionRepository extends JpaRepository<AssessmentOption, Long> {

  List<AssessmentOption> findByQuestionIdInOrderByQuestionIdAscOptionNoAsc(List<Long> questionIds);
}
