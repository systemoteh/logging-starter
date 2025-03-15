package ru.systemoteh.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.systemoteh.properties.HttpLoggingProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

public class HttpLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);
    private final HttpLoggingProperties properties;

    public HttpLoggingFilter(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestResponse(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestResponse(ContentCachingRequestWrapper request,
                                    ContentCachingResponseWrapper response,
                                    long duration) {

        String requestBody = getContent(request.getContentAsByteArray());
        String responseBody = getContent(response.getContentAsByteArray());

        String message = buildLogMessage(request, response, duration, requestBody, responseBody);

        switch (properties.getLevel()) {
            case TRACE -> log.trace(message);
            case DEBUG -> log.debug(message);
            default -> log.info(message);
        }
    }

    private String buildLogMessage(ContentCachingRequestWrapper request,
                                   ContentCachingResponseWrapper response,
                                   long duration,
                                   String requestBody,
                                   String responseBody) {

        StringBuilder msg = new StringBuilder("\n")
                .append("=== HTTP Log ===\n")
                .append("Method: ").append(request.getMethod()).append("\n")
                .append("URI: ").append(request.getRequestURI()).append("\n")
                .append("Status: ").append(response.getStatus()).append("\n")
                .append("Duration: ").append(duration).append("ms\n");

        if (properties.isIncludeHeaders()) {
            msg.append("Headers:\n")
                    .append(Collections.list(request.getHeaderNames())
                            .stream()
                            .map(h -> h + ": " + request.getHeader(h))
                            .collect(Collectors.joining("\n")))
                    .append("\n");
        }

        if (properties.isIncludeBody()) {
            msg.append("Request Body: ").append(requestBody).append("\n")
                    .append("Response Body: ").append(responseBody).append("\n");
        }

        return msg.append("=================").toString();
    }

    private String getContent(byte[] content) {
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "[empty]";
    }
}