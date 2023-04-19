package com.ttrip.api.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataResDto<T> extends BaseResDto {
    private T data;

    @Builder
    public DataResDto(int status, String message, T data) {
        super(status, message);
        this.data = data;
    }
}
