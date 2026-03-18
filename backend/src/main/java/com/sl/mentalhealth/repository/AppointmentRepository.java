package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  long countByScheduleIdAndAppointmentDateAndStatusIn(Long scheduleId,
      LocalDate appointmentDate,
      Collection<String> statuses);

  boolean existsByStudentAccountAndScheduleIdAndAppointmentDateAndStatusIn(String studentAccount,
      Long scheduleId,
      LocalDate appointmentDate,
      Collection<String> statuses);

  /**
   * 同一个学生，同一天，同一时间段，只能预约一个老师
   */
  boolean existsByStudentAccountAndAppointmentDateAndStartTimeAndEndTimeAndStatusIn(
      String studentAccount,
      LocalDate appointmentDate,
      LocalTime startTime,
      LocalTime endTime,
      Collection<String> statuses
  );

  List<Appointment> findByStudentAccountOrderByAppointmentDateDescStartTimeDesc(String studentAccount);

  Optional<Appointment> findByIdAndStudentAccount(Long id, String studentAccount);

  Optional<Appointment> findByIdAndTeacherAccount(Long id, String teacherAccount);

  @Query("""
            select a from Appointment a
            where a.teacherAccount = :teacherAccount
              and (:status is null or :status = '' or a.status = :status)
              and (:appointmentDate is null or a.appointmentDate = :appointmentDate)
            order by a.appointmentDate desc, a.startTime desc
            """)
  List<Appointment> findTeacherAppointments(@Param("teacherAccount") String teacherAccount,
      @Param("status") String status,
      @Param("appointmentDate") LocalDate appointmentDate);
}