package com.ttrip.api.dto.Call;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CallPayloadDto {
    private String type;
    private String sessionId;
    private String memberUuid;
    private String otherUuid;

    @Builder
    public CallPayloadDto(String type, String sessionId, String memberUuid, String otherUuid) {
        this.type = type;
        this.sessionId = sessionId;
        this.memberUuid = memberUuid;
        this.otherUuid = otherUuid;
    }
}