package io.github.valossa515.skypredict_java.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.skypredict.python")
public record SkyPredictPythonProperties(String publicUrl) {
}
