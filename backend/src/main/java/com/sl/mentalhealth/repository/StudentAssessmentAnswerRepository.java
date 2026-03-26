package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.StudentAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAssessmentAnswerRepository extends JpaRepository<StudentAssessmentAnswer, Long> {

  void deleteByRecordId(Long recordId);
}