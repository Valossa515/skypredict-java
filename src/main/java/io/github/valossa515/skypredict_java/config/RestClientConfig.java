package io.github.valossa515.skypredict_java.config;

import io.github.valossa515.skypredict_java.dataprovider.client.SkyPredictHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient skyPredictRestClient(@Value("${skypredict.python.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public SkyPredictHttpClient skyPredictHttpClient(RestClient skyPredictRestClient){
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(skyPredictRestClient))
                .build();
        return factory.createClient(SkyPredictHttpClient.class);
    }
}