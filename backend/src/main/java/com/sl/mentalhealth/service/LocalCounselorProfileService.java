package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.repository.CounselorRepository;
import com.sl.mentalhealth.vo.CounselorProfileResponseVO;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LocalCounselorProfileService {

  private final CounselorRepository counselorRepository;

  public LocalCounselorProfileService(CounselorRepository counselorRepository) {
    this.counselorRepository = counselorRepository;
  }

  public CounselorProfileResponseVO getProfile(String account) {
    Optional<Counselor> optional = counselorRepository.findByAccount(account);
    if (optional.isEmpty()) {
      return null;
    }

    Counselor counselor = optional.get();
    CounselorProfileResponseVO vo = new CounselorProfileResponseVO();
    vo.setCounselorId(counselor.getAccount());
    vo.setName(counselor.getName());
    vo.setCollege(counselor.getCollege());
    vo.setGrade(counselor.getGrade());
    vo.setPhone(counselor.getPhone());
    vo.setAvatarUrl(counselor.getAvatarUrl());
    return vo;
  }

  public CounselorProfileResponseVO updateAvatar(String account, String avatarUrl) {
    Optional<Counselor> optional = counselorRepository.findByAccount(account);
    if (optional.isEmpty()) {
      return null;
    }

    Counselor counselor = optional.get();
    counselor.setAvatarUrl(avatarUrl);
    counselorRepository.save(counselor);

    CounselorProfileResponseVO vo = new CounselorProfileResponseVO();
    vo.setCounselorId(counselor.getAccount());
    vo.setName(counselor.getName());
    vo.setCollege(counselor.getCollege());
    vo.setGrade(counselor.getGrade());
    vo.setPhone(counselor.getPhone());
    vo.setAvatarUrl(counselor.getAvatarUrl());
    return vo;
  }
}