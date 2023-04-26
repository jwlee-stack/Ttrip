package com.ttrip.api.dto;

import lombok.Data;

@Data
public class SearchReqDto {
    private Integer condition;
    private String nation;
    private String city;
    private String keyword;
}
