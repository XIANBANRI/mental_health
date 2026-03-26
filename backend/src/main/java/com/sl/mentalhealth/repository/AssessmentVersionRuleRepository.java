package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentVersionRuleRepository extends JpaRepository<AssessmentVersionRule, Long> {

  List<AssessmentVersionRule> findByVersionIdOrderByMinScoreAsc(Long versionId);

  Optional<AssessmentVersionRule> findFirstByVersionIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
      Long versionId, Integer minScore, Integer maxScore);
}