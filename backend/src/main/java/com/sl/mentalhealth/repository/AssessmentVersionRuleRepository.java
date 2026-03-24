package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentVersionRule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentVersionRuleRepository extends JpaRepository<AssessmentVersionRule, Long> {

  List<AssessmentVersionRule> findByVersionIdOrderByMinScoreAsc(Long versionId);
}