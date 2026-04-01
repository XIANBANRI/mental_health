package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "appointment_no")
  private String appointmentNo;

  @Column(name = "student_account")
  private String studentAccount;

  @Column(name = "teacher_account")
  private String teacherAccount;

  @Column(name = "schedule_id", nullable = false)
  private Long scheduleId;

  @Column(name = "appointment_date", nullable = false)
  private LocalDate appointmentDate;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Column(name = "purpose", length = 255)
  private String purpose;

  @Column(name = "remark", length = 500)
  private String remark;

  @Column(name = "teacher_reply", length = 500)
  private String teacherReply;

  @Column(name = "reject_reason", length = 500)
  private String rejectReason;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "approved_at")
  private LocalDateTime approvedAt;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @PrePersist
  public void prePersist() {
    if (status == null || status.trim().isEmpty()) {
      status = "PENDING";
    }
  }
}