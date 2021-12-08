package com.oyo.score.web.filter;

import com.oyo.score.web.context.OyoContext;
import com.oyo.score.web.utils.DateUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Clock;
import java.time.ZoneId;

@Component
public class RequestContextFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).contextWrite(context ->
                materializeContext(exchange.getRequest(), exchange, context));
    }

    private Context materializeContext(ServerHttpRequest request, ServerWebExchange exchange, Context context) {
        HttpHeaders headers = request.getHeaders();
        OyoContext oyoContext = OyoContext.builder()
                .uri(request.getURI())
                .method(request.getMethod())
                .clientId(headers.getFirst("X-ClientId"))
                .host(headers.getFirst(HttpHeaders.HOST))
                .userAgent(headers.getFirst(HttpHeaders.USER_AGENT))
                .startTime(DateUtil.currentTimeMillis())
                .detailedLog(!ObjectUtils.isEmpty(headers.getFirst("detailedLog")) &&
                        Boolean.parseBoolean(headers.getFirst("detailedLog")))
                .build();
        return context.put("oyoContext", oyoContext);
    }
}
