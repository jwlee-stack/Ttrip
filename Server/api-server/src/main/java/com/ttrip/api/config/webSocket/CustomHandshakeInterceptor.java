package com.ttrip.api.config.webSocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String path = servletRequest.getURI().getPath();
            // ["", "ws", "live", "{city}", "{memberId}"],
            // ["", "ws", "chat", "{chatroomId}", "{memberUuid}", "{opponentUuid}"]
            // ["", "ws", "match", "{memberUuid}","{opponentUuid}"]
            // ["", "ws", "call", "{memberUuid}"]
            String[] pathVariables = path.split("/");
            if (pathVariables[2].equals("live")) {
                attributes.put("city", pathVariables[3]);
                attributes.put("memberUuid", pathVariables[4]);
            } else if (pathVariables[2].equals("chat")) {
                attributes.put("chatroomId", pathVariables[3]);
                attributes.put("memberUuid", pathVariables[4]);
                attributes.put("opponentUuid", pathVariables[5]);
            } else if (pathVariables[2].equals("match")) {
                attributes.put("memberUuid", pathVariables[3]);
                attributes.put("opponentUuid", pathVariables[4]);
            } else if (pathVariables[2].equals("call")) {
                attributes.put("memberUuid", pathVariables[3]);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
