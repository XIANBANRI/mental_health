package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentResultRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentResultRuleRepository extends JpaRepository<AssessmentResultRule, Long> {

  Optional<AssessmentResultRule> findFirstByScaleIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
      Long scaleId, Integer minScore, Integer maxScore
  );
}
