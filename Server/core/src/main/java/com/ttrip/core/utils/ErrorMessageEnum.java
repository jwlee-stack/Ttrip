package com.ttrip.core.utils;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {

    USER_NOT_EXIST("존재하지 않는 회원입니다"),
    ARTICLE_NOT_EXIST("존재하지 않는 게시글입니다"),
    UNEXPECT_VALUE("잘못된 값입니다"),
    NO_AUTH("권한이 없습니다"),
    SERVER_ERROR("원인을 알 수 없습니다"),
    ARTICLE_NOT_EXIST("존재하지 않는 게시글입니다"),
    EXISTS_ACCOUNT("이미 가입한 유저입니다."),
    FAIL_TO_LOGOUT("리프래시 토큰 삭제 실패"),
    FAIL_TO_SIGNUP("회원가입 실패");
    private String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }
}
