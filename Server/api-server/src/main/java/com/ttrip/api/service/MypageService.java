package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.BackgroundUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.InfoUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.ProfileUpdateReqDto;
import com.ttrip.api.service.impl.MemberDetails;

public interface MypageService {
    DataResDto<?> viewMyArticles(MemberDetails memberDetails);
    DataResDto<?> updateMember(InfoUpdateReqDto infoUpdateReqDto,MemberDetails memberDetails);
    DataResDto<?> updateProfileImg(ProfileUpdateReqDto profileUpdateReqDto, MemberDetails memberDetails);
    DataResDto<?> updateBackgroundImg(BackgroundUpdateReqDto backGroundUpdateReqDto, MemberDetails memberDetails);
}
