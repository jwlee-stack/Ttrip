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
            // ["", "ws", "live", "{city}", "{memberId}"]
            String[] pathVariables = path.split("/");
            attributes.put("city", pathVariables[3]);
            attributes.put("memberUuid", pathVariables[4]);
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
