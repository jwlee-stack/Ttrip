package com.ttrip.core.customEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");

    @JsonCreator
    public static Gender from(String s)
    {
        return Gender.valueOf(s.toUpperCase());
    }

    private static final Map<String, Gender> paramMap =
            Arrays.stream(Gender.values())
                    .collect(Collectors.toMap(
                            Gender::getParam,
                            Function.identity()
                    ));

    private final String param;

    Gender(String param){
        this.param = param;
    }

    @JsonCreator
    public static Gender fromParam(String param){
        return Optional.ofNullable(param)
                .map(paramMap::get)
                .orElseThrow(() -> new RuntimeException("'MALE' 또는 'FEMALE'을 입력해주세요."));
    }

    @JsonValue
    public String getParam(){
        return this.param;
    }
}
