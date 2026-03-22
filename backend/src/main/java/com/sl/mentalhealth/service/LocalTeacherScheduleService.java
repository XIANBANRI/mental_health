package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.TeacherScheduleDeleteRequest;
import com.sl.mentalhealth.dto.TeacherScheduleQueryRequest;
import com.sl.mentalhealth.dto.TeacherScheduleSaveRequest;
import com.sl.mentalhealth.entity.TeacherSchedule;
import com.sl.mentalhealth.repository.AppointmentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.repository.TeacherScheduleRepository;
import com.sl.mentalhealth.vo.TeacherScheduleVO;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LocalTeacherScheduleService {

  private static final Integer STATUS_ENABLED = 1;
  private static final Integer STATUS_DISABLED = 0;

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * 这些状态视为终态，不阻止停用工作时间
   */
  private static final Set<String> FINISHED_APPOINTMENT_STATUSES = Set.of(
      "COMPLETED",
      "REJECTED",
      "CANCELLED"
  );

  private final TeacherScheduleRepository teacherScheduleRepository;
  private final TeacherRepository teacherRepository;
  private final AppointmentRepository appointmentRepository;

  public List<TeacherScheduleVO> query(TeacherScheduleQueryRequest request) {
    validateTeacherAccount(request.getTeacherAccount());

    List<TeacherSchedule> list;
    if (request.getWeekDay() == null) {
      list = teacherScheduleRepository.findByTeacherAccountAndStatusOrderByWeekDayAscStartTimeAsc(
          request.getTeacherAccount(),
          STATUS_ENABLED
      );
    } else {
      validateWeekDay(request.getWeekDay());
      list = teacherScheduleRepository.findByTeacherAccountAndWeekDayAndStatusOrderByStartTimeAsc(
          request.getTeacherAccount(),
          request.getWeekDay(),
          STATUS_ENABLED
      );
    }

    return list.stream().map(this::toVO).toList();
  }

  public TeacherScheduleVO add(TeacherScheduleSaveRequest request) {
    validateTeacherAccount(request.getTeacherAccount());
    validateSaveRequest(request);

    LocalTime startTime = parseTime(request.getStartTime());
    LocalTime endTime = parseTime(request.getEndTime());

    if (!startTime.isBefore(endTime)) {
      throw new RuntimeException("开始时间必须早于结束时间");
    }

    boolean enabledExists =
        teacherScheduleRepository.existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
            request.getTeacherAccount(),
            request.getWeekDay(),
            startTime,
            endTime,
            STATUS_ENABLED
        );

    if (enabledExists) {
      throw new RuntimeException("该工作时间已存在，请勿重复添加");
    }

    TeacherSchedule disabledRecord =
        teacherScheduleRepository.findByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
            request.getTeacherAccount(),
            request.getWeekDay(),
            startTime,
            endTime,
            STATUS_DISABLED
        ).orElse(null);

    if (disabledRecord != null) {
      disabledRecord.setStatus(STATUS_ENABLED);
      disabledRecord.setMaxAppointments(request.getMaxAppointments());
      disabledRecord.setRemark(request.getRemark());
      TeacherSchedule saved = teacherScheduleRepository.save(disabledRecord);
      return toVO(saved);
    }

    TeacherSchedule entity = new TeacherSchedule();
    entity.setTeacherAccount(request.getTeacherAccount());
    entity.setWeekDay(request.getWeekDay());
    entity.setStartTime(startTime);
    entity.setEndTime(endTime);
    entity.setMaxAppointments(request.getMaxAppointments());
    entity.setRemark(request.getRemark());
    entity.setStatus(STATUS_ENABLED);

    TeacherSchedule saved = teacherScheduleRepository.save(entity);
    return toVO(saved);
  }

  public TeacherScheduleVO update(TeacherScheduleSaveRequest request) {
    validateTeacherAccount(request.getTeacherAccount());
    validateSaveRequest(request);

    if (request.getId() == null) {
      throw new RuntimeException("工作时间ID不能为空");
    }

    LocalTime startTime = parseTime(request.getStartTime());
    LocalTime endTime = parseTime(request.getEndTime());

    if (!startTime.isBefore(endTime)) {
      throw new RuntimeException("开始时间必须早于结束时间");
    }

    TeacherSchedule entity = teacherScheduleRepository.findByIdAndTeacherAccount(
            request.getId(),
            request.getTeacherAccount()
        )
        .orElseThrow(() -> new RuntimeException("未找到对应的工作时间记录"));

    boolean exists =
        teacherScheduleRepository.existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatusAndIdNot(
            request.getTeacherAccount(),
            request.getWeekDay(),
            startTime,
            endTime,
            STATUS_ENABLED,
            request.getId()
        );

    if (exists) {
      throw new RuntimeException("修改后的工作时间与已有记录重复");
    }

    entity.setWeekDay(request.getWeekDay());
    entity.setStartTime(startTime);
    entity.setEndTime(endTime);
    entity.setMaxAppointments(request.getMaxAppointments());
    entity.setRemark(request.getRemark());
    entity.setStatus(STATUS_ENABLED);

    TeacherSchedule saved = teacherScheduleRepository.save(entity);
    return toVO(saved);
  }

  public void delete(TeacherScheduleDeleteRequest request) {
    if (request.getId() == null) {
      throw new RuntimeException("工作时间ID不能为空");
    }
    validateTeacherAccount(request.getTeacherAccount());

    TeacherSchedule entity = teacherScheduleRepository.findByIdAndTeacherAccount(
            request.getId(),
            request.getTeacherAccount()
        )
        .orElseThrow(() -> new RuntimeException("未找到对应的工作时间记录"));

    long activeAppointmentCount = appointmentRepository.countByScheduleIdAndStatusNotIn(
        entity.getId(),
        FINISHED_APPOINTMENT_STATUSES
    );

    if (activeAppointmentCount > 0) {
      throw new RuntimeException("该工作时间存在未完成的预约记录，无法删除。只有已完成、已拒绝或已取消的预约全部处理完后才可删除。");
    }

    entity.setStatus(STATUS_DISABLED);
    teacherScheduleRepository.save(entity);
  }

  private void validateTeacherAccount(String teacherAccount) {
    if (!StringUtils.hasText(teacherAccount)) {
      throw new RuntimeException("老师账号不能为空");
    }

    teacherRepository.findByAccount(teacherAccount)
        .orElseThrow(() -> new RuntimeException("老师账号不存在"));
  }

  private void validateSaveRequest(TeacherScheduleSaveRequest request) {
    if (request.getWeekDay() == null) {
      throw new RuntimeException("星期不能为空");
    }
    validateWeekDay(request.getWeekDay());

    if (!StringUtils.hasText(request.getStartTime())) {
      throw new RuntimeException("开始时间不能为空");
    }
    if (!StringUtils.hasText(request.getEndTime())) {
      throw new RuntimeException("结束时间不能为空");
    }
    if (request.getMaxAppointments() == null || request.getMaxAppointments() < 1) {
      throw new RuntimeException("最大预约人数必须大于0");
    }
  }

  private void validateWeekDay(Integer weekDay) {
    if (weekDay < 1 || weekDay > 7) {
      throw new RuntimeException("星期参数不合法");
    }
  }

  private LocalTime parseTime(String timeText) {
    String text = timeText.trim();
    if (text.length() == 5) {
      return LocalTime.parse(text, SHORT_TIME_FORMATTER);
    }
    return LocalTime.parse(text, TIME_FORMATTER);
  }

  private TeacherScheduleVO toVO(TeacherSchedule entity) {
    return new TeacherScheduleVO(
        entity.getId(),
        entity.getTeacherAccount(),
        entity.getWeekDay(),
        entity.getStartTime() == null ? null : entity.getStartTime().toString(),
        entity.getEndTime() == null ? null : entity.getEndTime().toString(),
        entity.getMaxAppointments(),
        entity.getRemark()
    );
  }
}