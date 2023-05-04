package com.ttrip.core.utils;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {

    ARTICLE_NOT_EXIST("존재하지 않는 게시글입니다"),
    UNEXPECT_VALUE("잘못된 값입니다"),
    NO_AUTH("권한이 없습니다"),
    SERVER_ERROR("원인을 알 수 없습니다"),
    EXISTS_ACCOUNT("이미 가입한 유저입니다."),
    FAIL_TO_LOGOUT("리프래시 토큰 삭제 실패"),
    NO_AUTH_TOKEN("권한 정보가 없는 토큰입니다."),
    FAIL_TO_SIGNUP("회원가입 실패"),
    SESSION_DISCONNECTED_BY_ERROR("session is disconnected by unexpected Error."),
    USER_NOT_EXIST("존재하지 않는 회원입니다"),
    MATCH_NOT_EXIST("존재하지 않는 매칭 이력입니다."),
    CHATMEMBER_NOT_EXIST("존재하지않는 채팅입니다."),
    ALREADY_CALLING("이미 통화중인 상대입니다."),
    FAILD_TO_TOKEN("토큰 발급에 실패하였습니다."),
    SURVEY_REQUIRED("여행 취향 설문 조사를 진행해 주세요");
    private String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }
}
