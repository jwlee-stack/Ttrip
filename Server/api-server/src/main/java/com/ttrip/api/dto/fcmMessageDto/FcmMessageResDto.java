package com.ttrip.api.dto.fcmMessageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class FcmMessageResDto {
    private boolean validate_only;
    private Message message;



    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Message {
        private Notification notification;
        private String token;
        private Map<String, String> data;

    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Notification {
        private String title;
        private String body;
    }
}
