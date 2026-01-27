package io.github.valossa515.skypredict_java.dataprovider.client;

import io.github.valossa515.skypredict_java.config.SkyPredictPythonProperties;
import io.github.valossa515.skypredict_java.core.domain.MapaSugeridoUrlResponse;
import io.github.valossa515.skypredict_java.core.gateway.SkyPredictGateway;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class SkyPredictGatewayHttpAdapter implements SkyPredictGateway {

    private final SkyPredictHttpClient httpClient;
    private final SkyPredictPythonProperties pythonProps;

    public SkyPredictGatewayHttpAdapter(SkyPredictHttpClient httpClient, SkyPredictPythonProperties pythonProps) {
        this.httpClient = httpClient;
        this.pythonProps = pythonProps;
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

    @Override
    public MapaSugeridoUrlResponse getMapaSugeridoUrl(String origemId, String destinoId, String data) {
        String base = trimTrailingSlash(pythonProps.publicUrl());

        String url = base + "/mapa_sugerido"
                + "?origem_id=" + enc(origemId)
                + "&destino_id=" + enc(destinoId)
                + "&data=" + enc(data);

        return new MapaSugeridoUrlResponse(url);
    }

    private static String enc(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

    private static String trimTrailingSlash(String s) {
        if (s == null) return "";
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}
