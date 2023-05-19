package com.ttrip.api.config.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final LiveHandler liveHandler;
    private final ChatHandler chatHandler;
    private final MatchHandler matchHandler;
    /**
     * WebSocket 핸들러를 등록하는 과정
     *
     * @param registry :
     *                 WebSocketHandlerRegistry 객체에 WebSocket 핸들러를 등록하는데 사용됩니다.
     *                 WebSocketHandlerRegistry는 스프링 웹소켓 프레임워크에서 웹소켓 핸들러를 관리하고 등록하는 데 사용되는 클래스입니다.
     *                 chatHandler라는 WebSocket 핸들러를 "/ws/chat" 경로에 연결합니다.
     *                 모든 도메인에서 이 웹소켓 서버에 접근할 수 있도록 허용합니다.
     *                 HttpSessionHandshakeInterceptor 인스턴스를 생성하여 WebSocket 세션에 HttpSession을 추가하는 인터셉터를 추가합니다
     *                 WebSocket 세션과 HttpSession 간의 데이터 공유가 가능해집니다.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(liveHandler, "ws/live/{city}/{memberUuid}").setAllowedOrigins("*")
                .addHandler(matchHandler, "ws/match/{memberUuid}/{opponentUuid}").setAllowedOrigins("*")
                .addHandler(chatHandler, "ws/chat/{chatroomId}/{memberUuid}/{opponentUuid}").setAllowedOrigins("*")
                .addInterceptors(new CustomHandshakeInterceptor());
    }

    /**
     * WebSocket Engine policy custom
     * 최대 유휴 시간 초과를 5분으로 설정합니다.
     * 자주 재연결을 시도하는 경우, 사용자 행동을 고려하여 유휴시간을 재조정해야 합니다.
     *
     * @return : WebSocket Engine policy
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout(Long.valueOf(1200000)); // 유휴 시간 초과를 20분으로 설정합니다.
        return container;
    }
}