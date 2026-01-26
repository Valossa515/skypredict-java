package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetAnaliseUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetAnaliseService implements GetAnaliseUseCase {

    private final SkyPredictGateway gateway;

    public GetAnaliseService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Map<String, Object> execute(double lat, double lon) {
        return gateway.getAnalise(lat, lon);
    }
}
