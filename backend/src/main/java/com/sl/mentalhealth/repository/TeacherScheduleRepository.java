package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.TeacherSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherScheduleRepository extends JpaRepository<TeacherSchedule, Long> {

  List<TeacherSchedule> findByWeekDayOrderByStartTimeAsc(Integer weekDay);
}