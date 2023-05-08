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
@ApiModel(value = "배경 사진 업데이트 응답")
public class BackgroundUpdateResDto {
    @ApiModelProperty(value = "배경 사진 경로", example = "/var/lib/images/background/aa.png")
    private String backgroundImgPath;

    public static BackgroundUpdateResDto toBuild(Member member)
    {
        return BackgroundUpdateResDto.builder()
                .backgroundImgPath(member.getBackgroundImgPath())
                .build();
    }


}
