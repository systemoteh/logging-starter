package ru.systemoteh.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.systemoteh.filter.HttpLoggingFilter;
import ru.systemoteh.properties.HttpLoggingProperties;

@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "logging.http", name = "enabled", matchIfMissing = true)
public class HttpLoggingAutoConfiguration {

    @Bean
    public HttpLoggingFilter httpLoggingFilter(HttpLoggingProperties properties) {
        return new HttpLoggingFilter(properties);
    }
}