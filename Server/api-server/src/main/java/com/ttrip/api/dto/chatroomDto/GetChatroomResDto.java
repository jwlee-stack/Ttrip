package com.ttrip.api.dto.chatroomDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@ApiModel(value = "GetChatroomResDto", description = "챗룸 조회시 받는 데이타")
public class GetChatroomResDto {
    @ApiModelProperty(value = "상대 닉네임", example = "김싸피")
    private String nickname;
    @ApiModelProperty(value = "상대 프로필주소", notes = "상대가 나갔으면 알수없음")
    private String imagePath;
    @ApiModelProperty(value = "상대의 uuid")
    private String memberUuid;
    @ApiModelProperty(value = "매칭 유사도", example = "78")
    private int similarity;
    @ApiModelProperty(value = "마지막 메세지", example = "그럼 거기서 봐요")
    private String lastMessage;
    @ApiModelProperty(value = "챗룸 id", example = "51")
    private Integer chatroomId;
    @ApiModelProperty(value = "마지막 메세지 전송시간", example = "2023-4-30T16:30:12.000")
    private LocalDateTime updatedAt;
    @ApiModelProperty(value = "관련 article 제목", example = "방콕가시는분 띱")
    private String articleTitle;
    @ApiModelProperty(value = "관련 article id", example = "1")
    private Integer articleId;
    @ApiModelProperty(value = "관련 article status",notes = "T모집중, F모집 완료, D 삭제됨", example = "T")
    private char status;
    @ApiModelProperty(value = "매칭 여부", example = "true")
    private Boolean isMatch;
}
