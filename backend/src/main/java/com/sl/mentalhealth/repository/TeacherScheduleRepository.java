package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.TeacherSchedule;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherScheduleRepository extends JpaRepository<TeacherSchedule, Long> {

  /**
   * 学生端：按星期查询启用中的工作时间
   */
  List<TeacherSchedule> findByWeekDayAndStatusOrderByStartTimeAsc(Integer weekDay, Integer status);

  /**
   * 老师端：查询本人启用中的全部工作时间
   */
  List<TeacherSchedule> findByTeacherAccountAndStatusOrderByWeekDayAscStartTimeAsc(
      String teacherAccount,
      Integer status
  );

  /**
   * 老师端：按星期查询本人启用中的工作时间
   */
  List<TeacherSchedule> findByTeacherAccountAndWeekDayAndStatusOrderByStartTimeAsc(
      String teacherAccount,
      Integer weekDay,
      Integer status
  );

  /**
   * 按ID和老师账号查询
   */
  Optional<TeacherSchedule> findByIdAndTeacherAccount(Long id, String teacherAccount);

  /**
   * 查询是否存在相同时间段且为指定状态
   */
  boolean existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
      String teacherAccount,
      Integer weekDay,
      LocalTime startTime,
      LocalTime endTime,
      Integer status
  );

  /**
   * 修改时排除自身后，校验是否与其他启用记录重复
   */
  boolean existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatusAndIdNot(
      String teacherAccount,
      Integer weekDay,
      LocalTime startTime,
      LocalTime endTime,
      Integer status,
      Long id
  );

  /**
   * 查找相同时间段且为指定状态的记录，用于恢复停用记录
   */
  Optional<TeacherSchedule> findByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
      String teacherAccount,
      Integer weekDay,
      LocalTime startTime,
      LocalTime endTime,
      Integer status
  );
}