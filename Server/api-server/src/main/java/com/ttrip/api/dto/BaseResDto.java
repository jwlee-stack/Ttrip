package com.ttrip.api.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResDto {

    private int status;
    private String message;

    public BaseResDto(int status, String message) {
        this.status = status == 0 ? 200 : status;
        this.message = message;
    }
}
