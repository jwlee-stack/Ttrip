package com.ttrip.api.dto.fcmMessageDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FcmMessageReqDto {
    private int type;
    private UUID targetUuid;

    private String extraId;
    private String extraData;
}
