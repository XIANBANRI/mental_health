package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentVersionQuestionRepository extends JpaRepository<AssessmentVersionQuestion, Long> {

  List<AssessmentVersionQuestion> findByVersionIdOrderByQuestionNoAsc(Long versionId);
}
