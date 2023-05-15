package com.ttrip.api.dto.fcmMessageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FromFlaskDto {
    private int type;
    private String nickname;
    private String extraData;
    private String result;

}
