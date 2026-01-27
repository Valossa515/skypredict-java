package io.github.valossa515.skypredict_java.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SkyPredictPythonProperties.class)
public class PropertiesConfig {
}
