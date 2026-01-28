package io.github.valossa515.skypredict_java.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.github.valossa515.skypredict_java.dataprovider.client.SkyPredictHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@TestConfiguration
public class TestRestClientConfig {

    @Autowired
    private WireMockServer wireMockServer;

    @Bean
    @Primary
    public RestClient testSkyPredictRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + wireMockServer.port())
                .build();
    }

    @Bean
    @Primary
    public SkyPredictHttpClient testSkyPredictHttpClient(RestClient testSkyPredictRestClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(testSkyPredictRestClient))
                .build();
        return factory.createClient(SkyPredictHttpClient.class);
    }

    @Bean
    @Primary
    public SkyPredictPythonProperties testSkyPredictPythonProperties() {
        return new SkyPredictPythonProperties("http://localhost:" + wireMockServer.port());
    }
}
