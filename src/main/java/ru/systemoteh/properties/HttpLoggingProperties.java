package ru.systemoteh.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "logging.http")
public class HttpLoggingProperties {
    private boolean enabled = true;
    private Level level = Level.INFO;
    private boolean includeHeaders = false;
    private boolean includeBody = false;

    public enum Level {
        INFO, DEBUG, TRACE
    }

    // Getters and Setters
}
