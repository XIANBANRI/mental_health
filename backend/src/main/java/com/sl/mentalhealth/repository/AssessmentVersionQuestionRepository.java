package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentVersionQuestionRepository extends JpaRepository<AssessmentVersionQuestion, Long> {

  List<AssessmentVersionQuestion> findByVersionIdOrderByQuestionNoAsc(Long versionId);
}