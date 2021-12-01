package com.sivalabs.bookmarks.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookmarks")
@Setter
@Getter
public class ApplicationProperties {
    private boolean importDataEnabled = true;
    private String importFilePath;
}
