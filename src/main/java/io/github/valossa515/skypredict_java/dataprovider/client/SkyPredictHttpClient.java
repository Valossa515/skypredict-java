package io.github.valossa515.skypredict_java.dataprovider.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange
public interface SkyPredictHttpClient {
    @GetExchange("/previsao")
    Map<String, Object> getPrevisao(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("data") String data);

    @GetExchange("/sugerir_rota")
    Map<String, Object> getSugerirRota(
            @RequestParam("origem_id") String origemId,
            @RequestParam("destino_id") String destinoId,
            @RequestParam("data") String data);

    @GetExchange("/analise")
    Map<String, Object> getAnalise(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon);

    @GetExchange("/graficos")
    byte[] getGraficos(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("data") String data);

    @GetExchange("/analise/graficos")
    byte[] getAnaliseGraficos(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon);

    @GetExchange("/exportar_excel")
    byte[] exportarExcel(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon);
}
