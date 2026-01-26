package io.github.valossa515.skypredict_java.core.gateway;

import java.util.Map;

public interface SkyPredictGateway {
    Map<String, Object> getPrevisao(double lat, double lon, String data);
    Map<String, Object> getSugerirRota(String origemId, String destinoId, String data);
    Map<String, Object> getAnalise(double lat, double lon);
    byte[] getGraficos(double lat, double lon, String data);
    byte[] getAnaliseGraficos(double lat, double lon);
    byte[] exportarExcel(double lat, double lon);
}
