package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {

  List<AssessmentQuestion> findByScaleIdOrderByQuestionNoAsc(Long scaleId);
}
