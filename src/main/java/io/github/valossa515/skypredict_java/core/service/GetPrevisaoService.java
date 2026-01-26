package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetPrevisaoUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetPrevisaoService implements GetPrevisaoUseCase {
    private final SkyPredictGateway gateway;

    public GetPrevisaoService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Map<String, Object> execute(double lat, double lon, String data) {
        return gateway.getPrevisao(lat, lon, data);
    }
}