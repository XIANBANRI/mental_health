package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentTotalRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentTotalRuleRepository extends JpaRepository<AssessmentTotalRule, Long> {

  Optional<AssessmentTotalRule> findFirstByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
      Integer minScore, Integer maxScore
  );
}
