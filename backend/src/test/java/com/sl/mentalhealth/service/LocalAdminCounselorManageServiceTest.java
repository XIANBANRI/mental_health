package com.sl.mentalhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sl.mentalhealth.dto.AdminCounselorClassesUpdateRequest;
import com.sl.mentalhealth.dto.AdminCounselorCreateRequest;
import com.sl.mentalhealth.dto.AdminCounselorQueryRequest;
import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.entity.CounselorClassMapping;
import com.sl.mentalhealth.kafka.message.AdminCounselorManageRequestMessage;
import com.sl.mentalhealth.kafka.message.AdminCounselorManageResponseMessage;
import com.sl.mentalhealth.repository.CounselorClassMappingRepository;
import com.sl.mentalhealth.repository.CounselorRepository;
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
class LocalAdminCounselorManageServiceTest {

    @Mock
    private CounselorRepository counselorRepository;

    @Mock
    private CounselorClassMappingRepository counselorClassMappingRepository;

    @InjectMocks
    private LocalAdminCounselorManageService service;

    @Test
    void handle_queryPage_success() {
        Counselor counselor = new Counselor();
        counselor.setAccount("c001");
        counselor.setName("王老师");
        counselor.setCollege("计算机学院");
        counselor.setGrade("2023");
        counselor.setPhone("13800000000");

        when(counselorRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(counselor), PageRequest.of(0, 10), 1));

        AdminCounselorQueryRequest queryRequest = new AdminCounselorQueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(10);

        AdminCounselorManageRequestMessage requestMessage = new AdminCounselorManageRequestMessage();
        requestMessage.setRequestId("r1");
        requestMessage.setAction(AdminCounselorManageRequestMessage.ACTION_QUERY_PAGE);
        requestMessage.setQueryRequest(queryRequest);

        AdminCounselorManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("查询成功", result.getMessage());
        assertEquals(1L, result.getPage().getTotal());
        assertEquals("c001", result.getPage().getList().get(0).getAccount());
    }

    @Test
    void handle_detail_success() {
        Counselor counselor = new Counselor();
        counselor.setAccount("c001");
        counselor.setName("王老师");
        counselor.setCollege("计算机学院");
        counselor.setGrade("2023");
        counselor.setPhone("13800000000");
        counselor.setAvatarUrl("/files/avatar/counselor/a.png");

        CounselorClassMapping mapping = new CounselorClassMapping();
        mapping.setCounselorAccount("c001");
        mapping.setClassName("软件1班");

        when(counselorRepository.findById("c001")).thenReturn(Optional.of(counselor));
        when(counselorClassMappingRepository.findByCounselorAccountOrderByClassNameAsc("c001"))
                .thenReturn(List.of(mapping));

        AdminCounselorManageRequestMessage requestMessage = new AdminCounselorManageRequestMessage();
        requestMessage.setRequestId("r2");
        requestMessage.setAction(AdminCounselorManageRequestMessage.ACTION_DETAIL);
        requestMessage.setAccount("c001");

        AdminCounselorManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("查询成功", result.getMessage());
        assertEquals("c001", result.getDetail().getAccount());
        assertEquals(1, result.getDetail().getClassList().size());
        assertEquals("软件1班", result.getDetail().getClassList().get(0));
    }

    @Test
    void handle_create_success() {
        Counselor counselor = new Counselor();
        counselor.setAccount("c001");
        counselor.setName("王老师");
        counselor.setCollege("计算机学院");
        counselor.setGrade("2023");
        counselor.setPhone("13800000000");

        CounselorClassMapping mapping = new CounselorClassMapping();
        mapping.setCounselorAccount("c001");
        mapping.setClassName("软件1班");

        when(counselorRepository.existsByAccount("c001")).thenReturn(false);
        when(counselorRepository.save(any(Counselor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(counselorRepository.findById("c001")).thenReturn(Optional.of(counselor));
        when(counselorClassMappingRepository.findFirstByClassName("软件1班")).thenReturn(Optional.empty());
        when(counselorClassMappingRepository.findByCounselorAccountOrderByClassNameAsc("c001"))
                .thenReturn(List.of(mapping));

        AdminCounselorCreateRequest createRequest = new AdminCounselorCreateRequest();
        createRequest.setAccount("c001");
        createRequest.setName("王老师");
        createRequest.setPassword("123456");
        createRequest.setCollege("计算机学院");
        createRequest.setGrade("2023");
        createRequest.setPhone("13800000000");
        createRequest.setClassList(List.of("软件1班"));

        AdminCounselorManageRequestMessage requestMessage = new AdminCounselorManageRequestMessage();
        requestMessage.setRequestId("r3");
        requestMessage.setAction(AdminCounselorManageRequestMessage.ACTION_CREATE);
        requestMessage.setCreateRequest(createRequest);

        AdminCounselorManageResponseMessage result = service.handle(requestMessage);

        assertTrue(result.isSuccess());
        assertEquals("新增成功", result.getMessage());
        assertEquals("c001", result.getDetail().getAccount());
        assertEquals("软件1班", result.getDetail().getClassList().get(0));
    }

    @Test
    void handle_updateClasses_conflict_returnsFailResponse() {
        Counselor counselor = new Counselor();
        counselor.setAccount("c001");

        CounselorClassMapping existing = new CounselorClassMapping();
        existing.setCounselorAccount("other001");
        existing.setClassName("软件1班");

        when(counselorRepository.findById("c001")).thenReturn(Optional.of(counselor));
        when(counselorClassMappingRepository.findFirstByClassName("软件1班"))
                .thenReturn(Optional.of(existing));

        AdminCounselorClassesUpdateRequest updateRequest = new AdminCounselorClassesUpdateRequest();
        updateRequest.setAccount("c001");
        updateRequest.setClassList(List.of("软件1班"));

        AdminCounselorManageRequestMessage requestMessage = new AdminCounselorManageRequestMessage();
        requestMessage.setRequestId("r4");
        requestMessage.setAction(AdminCounselorManageRequestMessage.ACTION_UPDATE_CLASSES);
        requestMessage.setClassesUpdateRequest(updateRequest);

        AdminCounselorManageResponseMessage result = service.handle(requestMessage);

        assertFalse(result.isSuccess());
        assertEquals("班级【软件1班】已分配给其他辅导员", result.getMessage());
    }

    @Test
    void handle_invalidAction_returnsFailResponse() {
        AdminCounselorManageRequestMessage requestMessage = new AdminCounselorManageRequestMessage();
        requestMessage.setRequestId("r5");
        requestMessage.setAction("UNKNOWN");

        AdminCounselorManageResponseMessage result = service.handle(requestMessage);

        assertFalse(result.isSuccess());
        assertEquals("不支持的操作类型", result.getMessage());
    }
}
