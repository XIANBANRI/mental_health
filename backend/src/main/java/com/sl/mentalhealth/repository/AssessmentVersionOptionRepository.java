package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentVersionOptionRepository extends JpaRepository<AssessmentVersionOption, Long> {

  List<AssessmentVersionOption> findByVersionQuestionIdIn(List<Long> versionQuestionIds);

  List<AssessmentVersionOption> findByVersionQuestionIdInOrderByVersionQuestionIdAscOptionNoAsc(
      List<Long> versionQuestionIds);

  List<AssessmentVersionOption> findByVersionQuestionIdOrderByOptionNoAsc(Long versionQuestionId);
}