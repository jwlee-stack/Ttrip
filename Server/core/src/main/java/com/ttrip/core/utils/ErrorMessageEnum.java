package com.ttrip.core.utils;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {

    USER_NOT_EXIST("존재하지 않는 회원입니다"),
    ARTICLE_NOT_EXIST("존재하지 않는 게시글입니다"),
    UNEXPECT_VALUE("잘못된 값입니다"),
    NO_AUTH("권한이 없습니다"),
    SERVER_ERROR("원인을 알수없습니다");
    private String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }
}
