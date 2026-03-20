package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Appointment;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.entity.TeacherSchedule;
import com.sl.mentalhealth.repository.AppointmentRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.repository.TeacherScheduleRepository;
import com.sl.mentalhealth.vo.AppointmentVO;
import com.sl.mentalhealth.vo.AvailableAppointmentVO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocalAppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final TeacherScheduleRepository teacherScheduleRepository;
  private final TeacherRepository teacherRepository;
  private final StudentRepository studentRepository;

  private static final List<String> OCCUPIED_STATUS = Arrays.asList("PENDING", "APPROVED", "COMPLETED");
  private static final List<String> DUPLICATE_STATUS = Arrays.asList("PENDING", "APPROVED");

  public List<AvailableAppointmentVO> studentAvailable(String dateStr) {
    LocalDate date = (dateStr == null || dateStr.isBlank()) ? LocalDate.now() : LocalDate.parse(dateStr);
    int weekDay = convertWeekDay(date.getDayOfWeek());

    List<TeacherSchedule> schedules = teacherScheduleRepository.findByWeekDayOrderByStartTimeAsc(weekDay);
    if (schedules.isEmpty()) {
      return Collections.emptyList();
    }

    Set<String> teacherAccounts = new HashSet<>();
    for (TeacherSchedule schedule : schedules) {
      teacherAccounts.add(schedule.getTeacherAccount());
    }

    Map<String, Teacher> teacherMap = new HashMap<>();
    for (Teacher teacher : teacherRepository.findAllById(teacherAccounts)) {
      teacherMap.put(teacher.getAccount(), teacher);
    }

    List<AvailableAppointmentVO> result = new ArrayList<>();
    for (TeacherSchedule schedule : schedules) {
      Teacher teacher = teacherMap.get(schedule.getTeacherAccount());

      long used = appointmentRepository.countByScheduleIdAndAppointmentDateAndStatusIn(
          schedule.getId(), date, OCCUPIED_STATUS
      );

      AvailableAppointmentVO vo = new AvailableAppointmentVO();
      vo.setScheduleId(schedule.getId());
      vo.setTeacherAccount(schedule.getTeacherAccount());
      vo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
      vo.setOfficeLocation(teacher == null ? null : teacher.getOfficeLocation());
      vo.setPhone(teacher == null ? null : teacher.getPhone());
      vo.setWeekDay(schedule.getWeekDay());
      vo.setStartTime(schedule.getStartTime() == null ? null : schedule.getStartTime().toString());
      vo.setEndTime(schedule.getEndTime() == null ? null : schedule.getEndTime().toString());
      vo.setMaxAppointments(schedule.getMaxAppointments());
      vo.setUsedAppointments((int) used);
      vo.setRemainingAppointments(schedule.getMaxAppointments() - (int) used);
      vo.setRemark(schedule.getRemark());
      result.add(vo);
    }

    return result;
  }

  @Transactional(rollbackFor = Exception.class)
  public Long studentCreate(String studentId, Long scheduleId, String appointmentDateStr, String purpose, String remark) {
    if (studentId == null || studentId.isBlank()) {
      throw new RuntimeException("学生学号不能为空");
    }
    if (scheduleId == null) {
      throw new RuntimeException("排班ID不能为空");
    }
    if (appointmentDateStr == null || appointmentDateStr.isBlank()) {
      throw new RuntimeException("预约日期不能为空");
    }

    Student student = studentRepository.findById(studentId).orElse(null);
    if (student == null) {
      throw new RuntimeException("学生不存在");
    }

    LocalDate appointmentDate = LocalDate.parse(appointmentDateStr);
    if (appointmentDate.isBefore(LocalDate.now())) {
      throw new RuntimeException("不能预约过去日期");
    }

    TeacherSchedule schedule = teacherScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new RuntimeException("排班不存在"));

    int weekDay = convertWeekDay(appointmentDate.getDayOfWeek());
    if (!Objects.equals(schedule.getWeekDay(), weekDay)) {
      throw new RuntimeException("预约日期与老师排班星期不匹配");
    }

    boolean duplicated = appointmentRepository.existsByStudentAccountAndScheduleIdAndAppointmentDateAndStatusIn(
        studentId, scheduleId, appointmentDate, DUPLICATE_STATUS
    );
    if (duplicated) {
      throw new RuntimeException("你已预约过该时段");
    }

    boolean sameTimeDuplicated =
        appointmentRepository.existsByStudentAccountAndAppointmentDateAndStartTimeAndEndTimeAndStatusIn(
            studentId,
            appointmentDate,
            schedule.getStartTime(),
            schedule.getEndTime(),
            DUPLICATE_STATUS
        );
    if (sameTimeDuplicated) {
      throw new RuntimeException("同一天同一时间段只能预约一个老师");
    }

    long used = appointmentRepository.countByScheduleIdAndAppointmentDateAndStatusIn(
        scheduleId, appointmentDate, OCCUPIED_STATUS
    );
    if (used >= schedule.getMaxAppointments()) {
      throw new RuntimeException("该时段预约人数已满");
    }

    Appointment appointment = new Appointment();
    appointment.setAppointmentNo(generateAppointmentNo());
    appointment.setStudentAccount(studentId);
    appointment.setTeacherAccount(schedule.getTeacherAccount());
    appointment.setScheduleId(scheduleId);
    appointment.setAppointmentDate(appointmentDate);
    appointment.setStartTime(schedule.getStartTime());
    appointment.setEndTime(schedule.getEndTime());
    appointment.setPurpose(purpose);
    appointment.setRemark(remark);
    appointment.setStatus("PENDING");
    appointment.setCreatedAt(LocalDateTime.now());
    appointment.setUpdatedAt(LocalDateTime.now());

    appointmentRepository.save(appointment);
    return appointment.getId();
  }

  public List<AppointmentVO> studentMy(String studentId) {
    List<Appointment> list = appointmentRepository.findByStudentAccountOrderByAppointmentDateDescStartTimeDesc(studentId);
    return buildAppointmentVOList(list);
  }

  @Transactional(rollbackFor = Exception.class)
  public void studentCancel(Long appointmentId, String studentId) {
    Appointment appointment = appointmentRepository.findByIdAndStudentAccount(appointmentId, studentId)
        .orElseThrow(() -> new RuntimeException("预约记录不存在"));

    if ("CANCELLED".equals(appointment.getStatus())) {
      throw new RuntimeException("该预约已取消");
    }
    if ("REJECTED".equals(appointment.getStatus())) {
      throw new RuntimeException("该预约已被拒绝，不能取消");
    }
    if ("COMPLETED".equals(appointment.getStatus())) {
      throw new RuntimeException("该预约已完成，不能取消");
    }

    appointment.setStatus("CANCELLED");
    appointment.setCancelledAt(LocalDateTime.now());
    appointment.setUpdatedAt(LocalDateTime.now());
    appointmentRepository.save(appointment);
  }

  public List<AppointmentVO> teacherList(String teacherAccount, String status, String dateStr) {
    if (teacherAccount == null || teacherAccount.isBlank()) {
      throw new RuntimeException("老师账号不能为空");
    }

    LocalDate date = null;
    if (dateStr != null && !dateStr.isBlank()) {
      date = LocalDate.parse(dateStr);
    }

    List<Appointment> list = appointmentRepository.findTeacherAppointments(
        teacherAccount,
        null,
        status,
        date
    );
    return buildAppointmentVOList(list);
  }

  @Transactional(rollbackFor = Exception.class)
  public void teacherApprove(Long appointmentId, String teacherAccount, String teacherReply) {
    Appointment appointment = appointmentRepository.findByIdAndTeacherAccount(appointmentId, teacherAccount)
        .orElseThrow(() -> new RuntimeException("预约记录不存在"));

    if (!"PENDING".equals(appointment.getStatus())) {
      throw new RuntimeException("只有待审核预约才能通过");
    }

    appointment.setStatus("APPROVED");
    appointment.setTeacherReply(teacherReply);
    appointment.setApprovedAt(LocalDateTime.now());
    appointment.setUpdatedAt(LocalDateTime.now());

    appointmentRepository.save(appointment);
  }

  @Transactional(rollbackFor = Exception.class)
  public void teacherReject(Long appointmentId, String teacherAccount, String teacherReply) {
    Appointment appointment = appointmentRepository.findByIdAndTeacherAccount(appointmentId, teacherAccount)
        .orElseThrow(() -> new RuntimeException("预约记录不存在"));

    if (!"PENDING".equals(appointment.getStatus())) {
      throw new RuntimeException("只有待审核预约才能拒绝");
    }

    appointment.setStatus("REJECTED");
    appointment.setTeacherReply(teacherReply);
    appointment.setUpdatedAt(LocalDateTime.now());

    appointmentRepository.save(appointment);
  }

  @Transactional(rollbackFor = Exception.class)
  public void teacherComplete(Long appointmentId, String teacherAccount, String teacherReply) {
    Appointment appointment = appointmentRepository.findByIdAndTeacherAccount(appointmentId, teacherAccount)
        .orElseThrow(() -> new RuntimeException("预约记录不存在"));

    if (!"APPROVED".equals(appointment.getStatus())) {
      throw new RuntimeException("只有已通过预约才能完成");
    }

    appointment.setStatus("COMPLETED");
    appointment.setTeacherReply(teacherReply);
    appointment.setCompletedAt(LocalDateTime.now());
    appointment.setUpdatedAt(LocalDateTime.now());

    appointmentRepository.save(appointment);
  }

  private List<AppointmentVO> buildAppointmentVOList(List<Appointment> appointments) {
    if (appointments == null || appointments.isEmpty()) {
      return Collections.emptyList();
    }

    Set<String> teacherAccounts = new HashSet<>();
    Set<String> studentAccounts = new HashSet<>();

    for (Appointment item : appointments) {
      teacherAccounts.add(item.getTeacherAccount());
      studentAccounts.add(item.getStudentAccount());
    }

    Map<String, Teacher> teacherMap = new HashMap<>();
    for (Teacher teacher : teacherRepository.findAllById(teacherAccounts)) {
      teacherMap.put(teacher.getAccount(), teacher);
    }

    Map<String, Student> studentMap = new HashMap<>();
    for (Student student : studentRepository.findAllById(studentAccounts)) {
      studentMap.put(student.getStudentId(), student);
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    List<AppointmentVO> result = new ArrayList<>();
    for (Appointment item : appointments) {
      Teacher teacher = teacherMap.get(item.getTeacherAccount());
      Student student = studentMap.get(item.getStudentAccount());

      AppointmentVO vo = new AppointmentVO();
      vo.setId(item.getId());
      vo.setAppointmentNo(item.getAppointmentNo());

      vo.setStudentAccount(item.getStudentAccount());
      vo.setStudentName(student == null ? null : student.getName());
      vo.setCollege(student == null ? null : student.getCollege());
      vo.setClassName(student == null ? null : student.getClassName());

      vo.setTeacherAccount(item.getTeacherAccount());
      vo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
      vo.setOfficeLocation(teacher == null ? null : teacher.getOfficeLocation());
      vo.setTeacherPhone(teacher == null ? null : teacher.getPhone());

      vo.setScheduleId(item.getScheduleId());
      vo.setAppointmentDate(item.getAppointmentDate() == null ? null : item.getAppointmentDate().toString());
      vo.setStartTime(item.getStartTime() == null ? null : item.getStartTime().toString());
      vo.setEndTime(item.getEndTime() == null ? null : item.getEndTime().toString());

      vo.setPurpose(item.getPurpose());
      vo.setRemark(item.getRemark());
      vo.setTeacherReply(item.getTeacherReply());
      vo.setStatus(item.getStatus());

      vo.setCreatedAt(item.getCreatedAt() == null ? null : item.getCreatedAt().format(dateTimeFormatter));
      vo.setApprovedAt(item.getApprovedAt() == null ? null : item.getApprovedAt().format(dateTimeFormatter));
      vo.setCancelledAt(item.getCancelledAt() == null ? null : item.getCancelledAt().format(dateTimeFormatter));
      vo.setCompletedAt(item.getCompletedAt() == null ? null : item.getCompletedAt().format(dateTimeFormatter));

      result.add(vo);
    }

    return result;
  }

  private int convertWeekDay(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> 1;
      case TUESDAY -> 2;
      case WEDNESDAY -> 3;
      case THURSDAY -> 4;
      case FRIDAY -> 5;
      case SATURDAY -> 6;
      case SUNDAY -> 7;
    };
  }

  private String generateAppointmentNo() {
    return "APT" + System.currentTimeMillis();
  }
}