package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetSugerirRotaUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetSugerirRotaService implements GetSugerirRotaUseCase {

    private final SkyPredictGateway gateway;

    public GetSugerirRotaService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Map<String, Object> execute(String origemId, String destinoId, String data) {
        return gateway.getSugerirRota(origemId, destinoId, data);
    }
}
