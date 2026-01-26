package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetGraficosUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetGraficosService implements GetGraficosUseCase {
    private final SkyPredictGateway gateway;

    public GetGraficosService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public byte[] execute(double lat, double lon, String data) {
        return gateway.getGraficos(lat, lon, data);
    }
}
