package com.sl.mentalhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sl.mentalhealth.dto.AdminTeacherCreateRequest;
import com.sl.mentalhealth.dto.AdminTeacherQueryRequest;
import com.sl.mentalhealth.dto.AdminTeacherUpdateRequest;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.kafka.message.AdminTeacherManageRequestMessage;
import com.sl.mentalhealth.kafka.message.AdminTeacherManageResponseMessage;
import com.sl.mentalhealth.repository.TeacherRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class LocalAdminTeacherManageServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private LocalAdminTeacherManageService service;

    @Test
    void handle_queryPage_success() {
        Teacher teacher = new Teacher();
        teacher.setAccount("t001");
        teacher.setTeacherName("李老师");
        teacher.setOfficeLocation("A101");
        teacher.setPhone("13800000000");

        when(teacherRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(teacher), PageRequest.of(0, 10), 1));

        AdminTeacherQueryRequest queryRequest = new AdminTeacherQueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(10);

        AdminTeacherManageRequestMessage requestMessage = new AdminTeacherManageRequestMessage();
        requestMessage.setRequestId("r1");
        requestMessage.setAction(AdminTeacherManageRequestMessage.ACTION_QUERY_PAGE);
        requestMessage.setQueryRequest(queryRequest);

        AdminTeacherManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("查询成功", result.getMessage());
        assertEquals(1L, result.getPage().getTotal());
        assertEquals(1, result.getPage().getList().size());
        assertEquals("t001", result.getPage().getList().get(0).getAccount());
    }

    @Test
    void handle_detail_success() {
        Teacher teacher = new Teacher();
        teacher.setAccount("t001");
        teacher.setTeacherName("李老师");
        teacher.setOfficeLocation("A101");
        teacher.setPhone("13800000000");
        teacher.setAvatarUrl("/files/avatar/teacher/a.png");

        when(teacherRepository.findById("t001")).thenReturn(Optional.of(teacher));

        AdminTeacherManageRequestMessage requestMessage = new AdminTeacherManageRequestMessage();
        requestMessage.setRequestId("r2");
        requestMessage.setAction(AdminTeacherManageRequestMessage.ACTION_DETAIL);
        requestMessage.setAccount("t001");

        AdminTeacherManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("查询成功", result.getMessage());
        assertEquals("t001", result.getTeacher().getAccount());
        assertEquals("李老师", result.getTeacher().getTeacherName());
    }

    @Test
    void handle_create_duplicateAccount_returnsFailResponse() {
        AdminTeacherCreateRequest createRequest = new AdminTeacherCreateRequest();
        createRequest.setAccount("t001");
        createRequest.setPassword("123456");

        when(teacherRepository.existsByAccount("t001")).thenReturn(true);

        AdminTeacherManageRequestMessage requestMessage = new AdminTeacherManageRequestMessage();
        requestMessage.setRequestId("r3");
        requestMessage.setAction(AdminTeacherManageRequestMessage.ACTION_CREATE);
        requestMessage.setCreateRequest(createRequest);

        AdminTeacherManageResponseMessage result = service.handle(requestMessage);

        assertFalse(result.isSuccess());
        assertEquals("老师账号已存在", result.getMessage());
    }

    @Test
    void handle_update_success() {
        Teacher teacher = new Teacher();
        teacher.setAccount("t001");
        teacher.setPassword("old");

        when(teacherRepository.findById("t001")).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminTeacherUpdateRequest updateRequest = new AdminTeacherUpdateRequest();
        updateRequest.setAccount("t001");
        updateRequest.setPassword("new123");
        updateRequest.setTeacherName("新老师");
        updateRequest.setOfficeLocation("B201");
        updateRequest.setPhone("13900000000");
        updateRequest.setAvatarUrl("/files/avatar/teacher/new.png");

        AdminTeacherManageRequestMessage requestMessage = new AdminTeacherManageRequestMessage();
        requestMessage.setRequestId("r4");
        requestMessage.setAction(AdminTeacherManageRequestMessage.ACTION_UPDATE);
        requestMessage.setUpdateRequest(updateRequest);

        AdminTeacherManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("修改成功", result.getMessage());
        assertEquals("t001", result.getTeacher().getAccount());
        assertEquals("新老师", result.getTeacher().getTeacherName());
        assertEquals("B201", result.getTeacher().getOfficeLocation());
        assertEquals("13900000000", result.getTeacher().getPhone());
        assertEquals("/files/avatar/teacher/new.png", result.getTeacher().getAvatarUrl());
    }

    @Test
    void handle_invalidAction_returnsFailResponse() {
        AdminTeacherManageRequestMessage requestMessage = new AdminTeacherManageRequestMessage();
        requestMessage.setRequestId("r5");
        requestMessage.setAction("UNKNOWN");

        AdminTeacherManageResponseMessage result = service.handle(requestMessage);

        assertFalse(result.isSuccess());
        assertEquals("不支持的操作类型", result.getMessage());
    }
}
