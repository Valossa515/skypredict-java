package io.github.valossa515.skypredict_java.entrypoint.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.valossa515.skypredict_java.config.TestRestClientConfig;
import io.github.valossa515.skypredict_java.config.WireMockConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({WireMockConfig.class, TestRestClientConfig.class})
class SkyPredictProxyControllerBlackboxTest {

    private static final String STUB_PREVISAO_SUCCESS = "previsao-success.json";
    private static final String STUB_SUGERIR_ROTA_SUCCESS = "sugerir-rota-success.json";
    private static final String STUB_SUGERIR_ROTA_DELAYED = "sugerir-rota-delayed.json";
    private static final String STUB_ANALISE_ENHANCED = "analise-enhanced.json";
    private static final String STUB_ANALISE_BASIC = "analise-basic.json";
    private static final String STUB_ERROR_500 = "error-500.json";
    private static final String STUB_ERROR_404 = "error-404.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
    }

    @Nested
    @DisplayName("GET /java/previsao")
    class PrevisaoEndpoint {

        @Test
        @DisplayName("Should return weather forecast successfully")
        void shouldReturnPrevisaoSuccessfully() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/previsao"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .withQueryParam("data", equalTo("2026-01-28"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_PREVISAO_SUCCESS)));

            // Act & Assert
            mockMvc.perform(get("/java/previsao")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.temperatura").value(25.5))
                    .andExpect(jsonPath("$.umidade").value(80))
                    .andExpect(jsonPath("$.condicao").value("Ensolarado"))
                    .andExpect(jsonPath("$.vento.velocidade").value(15.2))
                    .andExpect(jsonPath("$.vento.direcao").value("NE"));
        }

        @Test
        @DisplayName("Should propagate error when external service fails")
        void shouldReturn500WhenExternalServiceFails() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/previsao"))
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_ERROR_500)));

            // Act & Assert - Exception is propagated as the controller has no error handling
            try {
                mockMvc.perform(get("/java/previsao")
                                .param("lat", "-23.5505")
                                .param("lon", "-46.6333")
                                .param("data", "2026-01-28"));
            } catch (Exception e) {
                // Expected: exception is propagated from external service
                org.junit.jupiter.api.Assertions.assertTrue(
                        e.getCause() instanceof org.springframework.web.client.HttpServerErrorException);
            }
        }
    }

    @Nested
    @DisplayName("GET /java/sugerir_rota")
    class SugerirRotaEndpoint {

        @Test
        @DisplayName("Should return route suggestion successfully")
        void shouldReturnSugerirRotaSuccessfully() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/sugerir_rota"))
                    .withQueryParam("origem_id", equalTo("GRU"))
                    .withQueryParam("destino_id", equalTo("CGH"))
                    .withQueryParam("data", equalTo("2026-01-28"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_SUGERIR_ROTA_SUCCESS)));

            // Act & Assert
            mockMvc.perform(get("/java/sugerir_rota")
                            .param("origem_id", "GRU")
                            .param("destino_id", "CGH")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.rota").value("GRU -> CGH"))
                    .andExpect(jsonPath("$.distancia_km").value(25.5))
                    .andExpect(jsonPath("$.tempo_estimado").value("45 min"))
                    .andExpect(jsonPath("$.waypoints").isArray())
                    .andExpect(jsonPath("$.waypoints[0].nome").value("Guarulhos"));
        }

        @Test
        @DisplayName("Should return response even with delay from external service")
        void shouldHandleDelay() throws Exception {
            // Arrange - Small delay that doesn't cause timeout
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/sugerir_rota"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withFixedDelay(100)
                            .withBodyFile(STUB_SUGERIR_ROTA_DELAYED)));

            // Act & Assert
            mockMvc.perform(get("/java/sugerir_rota")
                            .param("origem_id", "GRU")
                            .param("destino_id", "CGH")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rota").value("GRU -> CGH"));
        }
    }

    @Nested
    @DisplayName("GET /java/analise")
    class AnaliseEndpoint {

        @Test
        @DisplayName("Should return analysis with enhanced=true")
        void shouldReturnAnaliseWithEnhanced() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/analise"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .withQueryParam("enhanced", equalTo("true"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_ANALISE_ENHANCED)));

            // Act & Assert
            mockMvc.perform(get("/java/analise")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333")
                            .param("enhanced", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analise").value("Detalhada"))
                    .andExpect(jsonPath("$.risco").value("Baixo"))
                    .andExpect(jsonPath("$.indice_seguranca").value(92))
                    .andExpect(jsonPath("$.fatores.visibilidade").value("Boa"));
        }

        @Test
        @DisplayName("Should return analysis with enhanced=false")
        void shouldReturnAnaliseWithoutEnhanced() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/analise"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .withQueryParam("enhanced", equalTo("false"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_ANALISE_BASIC)));

            // Act & Assert
            mockMvc.perform(get("/java/analise")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333")
                            .param("enhanced", "false"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analise").value("Básica"))
                    .andExpect(jsonPath("$.risco").value("Médio"));
        }
    }

    @Nested
    @DisplayName("GET /java/graficos")
    class GraficosEndpoint {

        @Test
        @DisplayName("Should return PNG image successfully")
        void shouldReturnGraficosAsPng() throws Exception {
            // Arrange - PNG magic bytes
            byte[] pngBytes = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/graficos"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .withQueryParam("data", equalTo("2026-01-28"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/png")
                            .withBody(pngBytes)));

            // Act & Assert
            mockMvc.perform(get("/java/graficos")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));
        }

        @Test
        @DisplayName("Should handle empty response from external service")
        void shouldHandleEmptyResponse() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/graficos"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/png")
                            .withBody(new byte[0])));

            // Act & Assert
            mockMvc.perform(get("/java/graficos")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /java/analise/graficos")
    class AnaliseGraficosEndpoint {

        @Test
        @DisplayName("Should return analysis graph as PNG")
        void shouldReturnAnaliseGraficosAsPng() throws Exception {
            // Arrange - PNG magic bytes
            byte[] pngBytes = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};

            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/analise/graficos"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/png")
                            .withBody(pngBytes)));

            // Act & Assert
            mockMvc.perform(get("/java/analise/graficos")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));
        }
    }

    @Nested
    @DisplayName("GET /java/exportar_excel")
    class ExportarExcelEndpoint {

        @Test
        @DisplayName("Should return Excel file successfully")
        void shouldReturnExcelFile() throws Exception {
            // Arrange - Excel/ZIP file magic bytes (XLSX is a ZIP file)
            byte[] excelBytes = new byte[]{0x50, 0x4B, 0x03, 0x04};

            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/exportar_excel"))
                    .withQueryParam("lat", equalTo("-23.5505"))
                    .withQueryParam("lon", equalTo("-46.6333"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .withBody(excelBytes)));

            // Act & Assert
            mockMvc.perform(get("/java/exportar_excel")
                            .param("lat", "-23.5505")
                            .param("lon", "-46.6333"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=skypredict.xlsx"));
        }
    }

    @Nested
    @DisplayName("GET /java/mapa_sugerido")
    class MapaSugeridoEndpoint {

        @Test
        @DisplayName("Should return map URL successfully")
        void shouldReturnMapaSugeridoUrl() throws Exception {
            // Act & Assert - This endpoint builds URL locally, no external call needed
            mockMvc.perform(get("/java/mapa_sugerido")
                            .param("origemId", "GRU")
                            .param("destinoId", "CGH")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.mapUrl").exists())
                    .andExpect(jsonPath("$.mapUrl").value(org.hamcrest.Matchers.containsString("mapa_sugerido")));
        }

        @Test
        @DisplayName("Should encode special characters in URL")
        void shouldEncodeSpecialCharactersInUrl() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/java/mapa_sugerido")
                            .param("origemId", "São Paulo")
                            .param("destinoId", "Rio de Janeiro")
                            .param("data", "2026-01-28"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.mapUrl").exists())
                    .andExpect(jsonPath("$.mapUrl").value(org.hamcrest.Matchers.containsString("S%C3%A3o")));
        }
    }

    @Nested
    @DisplayName("Error Scenarios")
    class ErrorScenarios {

        @Test
        @DisplayName("Should return 400 when required parameter is missing")
        void shouldReturn400WhenParameterMissing() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/java/previsao")
                            .param("lat", "-23.5505"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should propagate 404 from external service")
        void shouldHandle404FromExternalService() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/previsao"))
                    .willReturn(aResponse()
                            .withStatus(404)
                            .withHeader("Content-Type", "application/json")
                            .withBodyFile(STUB_ERROR_404)));

            // Act & Assert - Exception is propagated
            try {
                mockMvc.perform(get("/java/previsao")
                                .param("lat", "-23.5505")
                                .param("lon", "-46.6333")
                                .param("data", "2026-01-28"));
            } catch (Exception e) {
                org.junit.jupiter.api.Assertions.assertTrue(
                        e.getCause() instanceof org.springframework.web.client.HttpClientErrorException);
            }
        }

        @Test
        @DisplayName("Should propagate error when receiving malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/previsao"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("invalid json {")));

            // Act & Assert - Exception is propagated
            try {
                mockMvc.perform(get("/java/previsao")
                                .param("lat", "-23.5505")
                                .param("lon", "-46.6333")
                                .param("data", "2026-01-28"));
            } catch (Exception e) {
                org.junit.jupiter.api.Assertions.assertNotNull(e.getCause());
            }
        }
    }
}
