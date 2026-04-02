package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeacherRepository extends JpaRepository<Teacher, String>, JpaSpecificationExecutor<Teacher> {

  Optional<Teacher> findByAccount(String account);

  boolean existsByAccount(String account);
}