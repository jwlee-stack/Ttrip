package com.ttrip.api.dto.memberDto.memberReqDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberReportReqDto {
    private String reportedNickname;
    private String reportContext;
    private Integer matchHistoryId;
}
