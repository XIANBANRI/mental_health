package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentVersionOptionRepository extends JpaRepository<AssessmentVersionOption, Long> {

  List<AssessmentVersionOption> findByVersionQuestionIdIn(List<Long> questionIds);

  List<AssessmentVersionOption> findByVersionQuestionIdOrderByOptionNoAsc(Long versionQuestionId);
}