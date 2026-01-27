package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.domain.MapaSugeridoUrlResponse;
import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.GetSugerirMapaUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetSugerirMapaService implements GetSugerirMapaUseCase {

    private final SkyPredictGateway gateway;

    public GetSugerirMapaService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public MapaSugeridoUrlResponse execute(String origemId, String destinoId, String data) {
        return gateway.getMapaSugeridoUrl(origemId, destinoId, data);
    }
}
