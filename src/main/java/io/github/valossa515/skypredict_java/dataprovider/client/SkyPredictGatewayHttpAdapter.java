package io.github.valossa515.skypredict_java.dataprovider.client;

import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SkyPredictGatewayHttpAdapter implements SkyPredictGateway {

    private final SkyPredictHttpClient httpClient;

    public SkyPredictGatewayHttpAdapter(SkyPredictHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, Object> getPrevisao(double lat, double lon, String data) {
        return httpClient.getPrevisao(lat, lon, data);
    }

    @Override
    public Map<String, Object> getSugerirRota(String origemId, String destinoId, String data) {
        return httpClient.getSugerirRota(origemId, destinoId, data);
    }

    @Override
    public Map<String, Object> getAnalise(double lat, double lon) {
        return httpClient.getAnalise(lat, lon);
    }

    @Override
    public byte[] getGraficos(double lat, double lon, String data) {
        return httpClient.getGraficos(lat, lon, data);
    }

    @Override
    public byte[] getAnaliseGraficos(double lat, double lon) {
        return httpClient.getAnaliseGraficos(lat, lon);
    }

    @Override
    public byte[] exportarExcel(double lat, double lon) {
        return httpClient.exportarExcel(lat, lon);
    }
}
