package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.TeacherSchedule;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherScheduleRepository extends JpaRepository<TeacherSchedule, Long> {

  /**
   * 原有：按星期查询全部老师的时间
   */
  List<TeacherSchedule> findByWeekDayOrderByStartTimeAsc(Integer weekDay);

  /**
   * 按老师账号查询全部工作时间
   */
  List<TeacherSchedule> findByTeacherAccountOrderByWeekDayAscStartTimeAsc(String teacherAccount);

  /**
   * 按老师账号 + 星期查询工作时间
   */
  List<TeacherSchedule> findByTeacherAccountAndWeekDayOrderByStartTimeAsc(
      String teacherAccount,
      Integer weekDay
  );

  /**
   * 按ID和老师账号查询，防止误改别人的记录
   */
  Optional<TeacherSchedule> findByIdAndTeacherAccount(Long id, String teacherAccount);

  /**
   * 新增时校验是否已存在相同时间段
   */
  boolean existsByTeacherAccountAndWeekDayAndStartTimeAndEndTime(
      String teacherAccount,
      Integer weekDay,
      LocalTime startTime,
      LocalTime endTime
  );

  /**
   * 修改时校验是否与其他记录重复
   */
  boolean existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndIdNot(
      String teacherAccount,
      Integer weekDay,
      LocalTime startTime,
      LocalTime endTime,
      Long id
  );
}