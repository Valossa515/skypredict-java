package io.github.valossa515.skypredict_java.entrypoint.api;

import io.github.valossa515.skypredict_java.core.domain.MapaSugeridoUrlResponse;
import io.github.valossa515.skypredict_java.core.usecases.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/java")
public class SkyPredictProxyController {
    private final GetPrevisaoUseCase getPrevisaoUseCase;
    private final GetSugerirRotaUseCase getSugerirRotaUseCase;
    private final GetAnaliseUseCase getAnaliseUseCase;
    private final GetGraficosUseCase getGraficosUseCase;
    private final GetAnaliseGraficosUseCase getAnaliseGraficosUseCase;
    private final ExportarExcelUseCase exportarExcelUseCase;
    private final GetSugerirMapaUseCase getSugerirMapaUseCase;

    public SkyPredictProxyController(
            GetPrevisaoUseCase getPrevisaoUseCase,
            GetSugerirRotaUseCase getSugerirRotaUseCase,
            GetAnaliseUseCase getAnaliseUseCase,
            GetGraficosUseCase getGraficosUseCase,
            GetAnaliseGraficosUseCase getAnaliseGraficosUseCase,
            ExportarExcelUseCase exportarExcelUseCase,
            GetSugerirMapaUseCase getSugerirMapaUseCase) {
        this.getPrevisaoUseCase = getPrevisaoUseCase;
        this.getSugerirRotaUseCase = getSugerirRotaUseCase;
        this.getAnaliseUseCase = getAnaliseUseCase;
        this.getGraficosUseCase = getGraficosUseCase;
        this.getAnaliseGraficosUseCase = getAnaliseGraficosUseCase;
        this.exportarExcelUseCase = exportarExcelUseCase;
        this.getSugerirMapaUseCase = getSugerirMapaUseCase;
    }

    @GetMapping("/previsao")
    public Map<String, Object> previsao(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String data) {
        return getPrevisaoUseCase.execute(lat, lon, data);
    }

    @GetMapping("/sugerir_rota")
    public Map<String, Object> sugerirRota(
            @RequestParam("origem_id") String origemId,
            @RequestParam("destino_id") String destinoId,
            @RequestParam String data) {
        return getSugerirRotaUseCase.execute(origemId, destinoId, data);
    }

    @GetMapping("/analise")
    public Map<String, Object> analise(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam boolean enhanced) {
        return getAnaliseUseCase.execute(lat, lon, enhanced);
    }

    @GetMapping(value = "/graficos", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> graficos(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String data) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(getGraficosUseCase.execute(lat, lon, data));
    }

    @GetMapping(value = "/analise/graficos", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> analiseGraficos(
            @RequestParam double lat,
            @RequestParam double lon) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(getAnaliseGraficosUseCase.execute(lat, lon));
    }

    @GetMapping(value = "/exportar_excel",
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarExcel(
            @RequestParam double lat,
            @RequestParam double lon) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=skypredict.xlsx")
                .body(exportarExcelUseCase.execute(lat, lon));
    }

    @GetMapping(value = "/mapa_sugerido")
    public ResponseEntity<MapaSugeridoUrlResponse> mapaSugerido(
            @RequestParam String origemId,
            @RequestParam String destinoId,
            @RequestParam String data
    ) {
       return ResponseEntity.ok()
                .body(getSugerirMapaUseCase.execute(origemId, destinoId, data));
    }
}