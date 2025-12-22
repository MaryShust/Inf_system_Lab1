package infs.lab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import io.minio.http.Method;

import java.util.concurrent.TimeUnit;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String adminName;
    private boolean enabledLogCache;
    private Method method;
    private int presignedUrlExpiryDays;
    private TimeUnit PresignedUrlExpiryTimeUnit;

}