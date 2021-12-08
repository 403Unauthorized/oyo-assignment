package com.oyo.score.web.context;

import org.springframework.http.HttpMethod;

import java.net.URI;

public class OyoContext {
    private URI uri;
    private HttpMethod method;
    private String clientId;
    private String host;
    private String userAgent;
    private Long startTime;
    private boolean detailedLog;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public boolean isDetailedLog() {
        return detailedLog;
    }

    public void setDetailedLog(boolean detailedLog) {
        this.detailedLog = detailedLog;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private URI uri;
        private HttpMethod method;
        private String clientId;
        private String host;
        private String userAgent;
        private Long startTime;
        private boolean detailedLog;

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder detailedLog(boolean detailedLog) {
            this.detailedLog = detailedLog;
            return this;
        }

        public OyoContext build() {
            OyoContext context = new OyoContext();
            context.uri = this.uri;
            context.method = this.method;
            context.clientId = this.clientId;
            context.host = this.host;
            context.userAgent = this.userAgent;
            context.startTime = this.startTime;
            context.detailedLog = this.detailedLog;
            return context;
        }
    }
}
