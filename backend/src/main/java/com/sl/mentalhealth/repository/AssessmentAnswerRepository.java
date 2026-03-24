package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, Long> {

  void deleteByRecordIdAndQuestionIdIn(Long recordId, List<Long> questionIds);
}
