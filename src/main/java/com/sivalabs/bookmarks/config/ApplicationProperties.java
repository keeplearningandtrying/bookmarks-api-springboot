package com.sivalabs.bookmarks.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookmarks")
@Data
public class ApplicationProperties {
    private boolean importDataEnabled = true;
    private String importFilePath;
}
