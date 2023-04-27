package com.ttrip.api.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ApplyReqDto {
    private Integer articleId;
    private String requestContent;
}
