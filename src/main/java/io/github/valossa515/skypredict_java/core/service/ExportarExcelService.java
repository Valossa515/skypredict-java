package io.github.valossa515.skypredict_java.core.service;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import io.github.valossa515.skypredict_java.core.usecases.ExportarExcelUseCase;
import org.springframework.stereotype.Service;

@Service
public class ExportarExcelService implements ExportarExcelUseCase {
    private final SkyPredictGateway gateway;

    public ExportarExcelService(SkyPredictGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public byte[] execute(double lat, double lon) {
        return gateway.exportarExcel(lat, lon);
    }
}