package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetAnaliseGraficosUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetAnaliseGraficosService implements GetAnaliseGraficosUseCase {

    private final SkyPredictGateway gateway;

    public GetAnaliseGraficosService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public byte[] execute(double lat, double lon) {
        return gateway.getAnaliseGraficos(lat, lon);
    }
}