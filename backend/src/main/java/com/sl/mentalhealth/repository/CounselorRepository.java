package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselorRepository extends JpaRepository<Counselor, String> {
}