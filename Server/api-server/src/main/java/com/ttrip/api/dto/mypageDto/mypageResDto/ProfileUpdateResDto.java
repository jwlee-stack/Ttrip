package com.ttrip.api.dto.mypageDto.mypageResDto;

import com.ttrip.core.entity.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel(value = "프로필 사진 업데이트 응답")
public class ProfileUpdateResDto {
    @ApiModelProperty(value = "프로필 사진 경로", example = "/var/lib/images/profile/aa.png")
    private String profileImgPath;
    @ApiModelProperty(value = "마커 이미지 경로", example = "/var/lib/images/marker/aa.png")
    private String markerImgPath;

    public static ProfileUpdateResDto toBuild(Member member)
    {
        return ProfileUpdateResDto.builder()
                .profileImgPath(member.getProfileImgPath())
                .markerImgPath(member.getMarkerImgPath())
                .build();
    }


}
