package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  long countByScheduleIdAndAppointmentDateAndStatusIn(
      Long scheduleId,
      LocalDate appointmentDate,
      Collection<String> statuses
  );

  long countByScheduleIdAndStatusNotIn(
      Long scheduleId,
      Collection<String> statuses
  );

  boolean existsByStudentAccountAndScheduleIdAndAppointmentDateAndStatusIn(
      String studentAccount,
      Long scheduleId,
      LocalDate appointmentDate,
      Collection<String> statuses
  );

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
              and a.status not in ('COMPLETED', 'CANCELLED')
              and (:studentAccount is null or :studentAccount = '' or a.studentAccount like concat('%', :studentAccount, '%'))
              and (:status is null or :status = '' or a.status = :status)
              and (:appointmentDate is null or a.appointmentDate = :appointmentDate)
            order by a.appointmentDate desc, a.startTime desc
            """)
  List<Appointment> findTeacherAppointments(
      @Param("teacherAccount") String teacherAccount,
      @Param("studentAccount") String studentAccount,
      @Param("status") String status,
      @Param("appointmentDate") LocalDate appointmentDate
  );

  @Query("""
            select a from Appointment a
            where a.teacherAccount = :teacherAccount
              and a.status <> :excludedStatus
            order by a.appointmentDate desc, a.startTime desc
            """)
  List<Appointment> findTeacherAppointmentRecords(
      @Param("teacherAccount") String teacherAccount,
      @Param("excludedStatus") String excludedStatus
  );

  @Query("""
            select a from Appointment a
            where a.teacherAccount = :teacherAccount
              and (:studentAccount is null or :studentAccount = '' or a.studentAccount like concat('%', :studentAccount, '%'))
              and (:appointmentDate is null or a.appointmentDate = :appointmentDate)
              and (
                   ((:status is null or :status = '') and a.status <> 'PENDING')
                   or
                   ((:status is not null and :status <> '') and a.status = :status)
              )
            order by a.appointmentDate desc, a.startTime desc
            """)
  List<Appointment> findTeacherAppointmentRecords(
      @Param("teacherAccount") String teacherAccount,
      @Param("studentAccount") String studentAccount,
      @Param("status") String status,
      @Param("appointmentDate") LocalDate appointmentDate
  );
}