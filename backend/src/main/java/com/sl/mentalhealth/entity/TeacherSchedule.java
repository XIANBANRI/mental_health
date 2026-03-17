package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "teacher_schedule")
public class TeacherSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "teacher_account", nullable = false, length = 50)
  private String teacherAccount;

  @Column(name = "week_day", nullable = false)
  private Integer weekDay;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Column(name = "max_appointments", nullable = false)
  private Integer maxAppointments;

  @Column(name = "remark", length = 255)
  private String remark;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}