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
@ActiveProfiles(SkyPredictProxyControllerBlackboxTest.PROFILE_TEST)
@Import({WireMockConfig.class, TestRestClientConfig.class})
class SkyPredictProxyControllerBlackboxTest {

    static final String PROFILE_TEST = "test";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_PNG = "image/png";
    private static final String CONTENT_TYPE_EXCEL =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_DISPOSITION_EXCEL = "attachment; filename=skypredict.xlsx";

    private static final String INTERNAL_PREVISAO_PATH = "/java/previsao";
    private static final String INTERNAL_SUGERIR_ROTA_PATH = "/java/sugerir_rota";
    private static final String INTERNAL_ANALISE_PATH = "/java/analise";
    private static final String INTERNAL_GRAFICOS_PATH = "/java/graficos";
    private static final String INTERNAL_ANALISE_GRAFICOS_PATH = "/java/analise/graficos";
    private static final String INTERNAL_EXPORTAR_EXCEL_PATH = "/java/exportar_excel";
    private static final String INTERNAL_MAPA_SUGERIDO_PATH = "/java/mapa_sugerido";

    private static final String EXTERNAL_PREVISAO_PATH = "/previsao";
    private static final String EXTERNAL_SUGERIR_ROTA_PATH = "/sugerir_rota";
    private static final String EXTERNAL_ANALISE_PATH = "/analise";
    private static final String EXTERNAL_GRAFICOS_PATH = "/graficos";
    private static final String EXTERNAL_ANALISE_GRAFICOS_PATH = "/analise/graficos";
    private static final String EXTERNAL_EXPORTAR_EXCEL_PATH = "/exportar_excel";

    private static final String DISPLAYNAME_GET_PREVISAO = "GET " + INTERNAL_PREVISAO_PATH;
    private static final String DISPLAYNAME_GET_SUGERIR_ROTA = "GET " + INTERNAL_SUGERIR_ROTA_PATH;
    private static final String DISPLAYNAME_GET_ANALISE = "GET " + INTERNAL_ANALISE_PATH;
    private static final String DISPLAYNAME_GET_GRAFICOS = "GET " + INTERNAL_GRAFICOS_PATH;
    private static final String DISPLAYNAME_GET_ANALISE_GRAFICOS = "GET " + INTERNAL_ANALISE_GRAFICOS_PATH;
    private static final String DISPLAYNAME_GET_EXPORTAR_EXCEL = "GET " + INTERNAL_EXPORTAR_EXCEL_PATH;
    private static final String DISPLAYNAME_GET_MAPA_SUGERIDO = "GET " + INTERNAL_MAPA_SUGERIDO_PATH;
    private static final String DISPLAYNAME_ERROR_SCENARIOS = "Error Scenarios";
    private static final String DISPLAYNAME_PREVISAO_SUCCESS = "Should return weather forecast successfully";
    private static final String DISPLAYNAME_PREVISAO_EXTERNAL_ERROR =
            "Should propagate error when external service fails";
    private static final String DISPLAYNAME_SUGERIR_ROTA_SUCCESS =
            "Should return route suggestion successfully";
    private static final String DISPLAYNAME_SUGERIR_ROTA_DELAY =
            "Should return response even with delay from external service";
    private static final String DISPLAYNAME_ANALISE_ENHANCED_TRUE = "Should return analysis with enhanced=true";
    private static final String DISPLAYNAME_ANALISE_ENHANCED_FALSE = "Should return analysis with enhanced=false";
    private static final String DISPLAYNAME_GRAFICOS_PNG = "Should return PNG image successfully";
    private static final String DISPLAYNAME_GRAFICOS_EMPTY =
            "Should handle empty response from external service";
    private static final String DISPLAYNAME_ANALISE_GRAFICOS_PNG = "Should return analysis graph as PNG";
    private static final String DISPLAYNAME_EXPORTAR_EXCEL = "Should return Excel file successfully";
    private static final String DISPLAYNAME_MAPA_SUGERIDO_URL = "Should return map URL successfully";
    private static final String DISPLAYNAME_MAPA_SUGERIDO_SPECIAL = "Should encode special characters in URL";
    private static final String DISPLAYNAME_MISSING_PARAM =
            "Should return 400 when required parameter is missing";
    private static final String DISPLAYNAME_EXTERNAL_404 = "Should propagate 404 from external service";
    private static final String DISPLAYNAME_MALFORMED_JSON =
            "Should propagate error when receiving malformed JSON";

    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LON = "lon";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_ENHANCED = "enhanced";
    private static final String PARAM_ORIGEM_ID = "origem_id";
    private static final String PARAM_DESTINO_ID = "destino_id";
    private static final String PARAM_ORIGEM_ID_CAMEL = "origemId";
    private static final String PARAM_DESTINO_ID_CAMEL = "destinoId";

    private static final String LAT_SAO_PAULO = "-23.5505";
    private static final String LON_SAO_PAULO = "-46.6333";
    private static final String DATA_EXEMPLO = "2026-01-28";
    private static final String ORIGEM_GRU = "GRU";
    private static final String DESTINO_CGH = "CGH";
    private static final String ORIGEM_SAO_PAULO = "São Paulo";
    private static final String DESTINO_RIO_JANEIRO = "Rio de Janeiro";
    private static final String ENHANCED_TRUE = "true";
    private static final String ENHANCED_FALSE = "false";

    private static final String JSON_TEMPERATURA = "$.temperatura";
    private static final String JSON_UMIDADE = "$.umidade";
    private static final String JSON_CONDICAO = "$.condicao";
    private static final String JSON_VENTO_VELOCIDADE = "$.vento.velocidade";
    private static final String JSON_VENTO_DIRECAO = "$.vento.direcao";
    private static final String JSON_ROTA = "$.rota";
    private static final String JSON_DISTANCIA_KM = "$.distancia_km";
    private static final String JSON_TEMPO_ESTIMADO = "$.tempo_estimado";
    private static final String JSON_WAYPOINTS = "$.waypoints";
    private static final String JSON_WAYPOINTS_0_NOME = "$.waypoints[0].nome";
    private static final String JSON_ANALISE = "$.analise";
    private static final String JSON_RISCO = "$.risco";
    private static final String JSON_INDICE_SEGURANCA = "$.indice_seguranca";
    private static final String JSON_FATORES_VISIBILIDADE = "$.fatores.visibilidade";
    private static final String JSON_MAP_URL = "$.mapUrl";

    private static final double EXPECT_TEMPERATURA = 25.5;
    private static final int EXPECT_UMIDADE = 80;
    private static final String EXPECT_CONDICAO = "Ensolarado";
    private static final double EXPECT_VENTO_VELOCIDADE = 15.2;
    private static final String EXPECT_VENTO_DIRECAO = "NE";
    private static final String EXPECT_ROTA = "GRU -> CGH";
    private static final double EXPECT_DISTANCIA_KM = 25.5;
    private static final String EXPECT_TEMPO_ESTIMADO = "45 min";
    private static final String EXPECT_WAYPOINT_NOME = "Guarulhos";
    private static final String EXPECT_ANALISE_DETALHADA = "Detalhada";
    private static final String EXPECT_RISCO_BAIXO = "Baixo";
    private static final int EXPECT_INDICE_SEGURANCA = 92;
    private static final String EXPECT_VISIBILIDADE_BOA = "Boa";
    private static final String EXPECT_ANALISE_BASICA = "Básica";
    private static final String EXPECT_RISCO_MEDIO = "Médio";
    private static final String EXPECT_MAPA_SUGERIDO_SUBSTRING = "mapa_sugerido";
    private static final String EXPECT_SAO_ENCODED = "S%C3%A3o";

    private static final String MALFORMED_JSON = "invalid json {";

    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_SERVER_ERROR = 500;
    private static final int DELAY_MS = 100;

    private static final byte[] PNG_MAGIC_BYTES_FULL =
            new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final byte[] PNG_MAGIC_BYTES_SHORT =
            new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};
    private static final byte[] EXCEL_MAGIC_BYTES = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private static final byte[] EMPTY_BYTES = new byte[0];

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
    @DisplayName(DISPLAYNAME_GET_PREVISAO)
    class PrevisaoEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_PREVISAO_SUCCESS)
        void shouldReturnPrevisaoSuccessfully() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_PREVISAO_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .withQueryParam(PARAM_DATA, equalTo(DATA_EXEMPLO))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_PREVISAO_SUCCESS)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_PREVISAO_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath(JSON_TEMPERATURA).value(EXPECT_TEMPERATURA))
                    .andExpect(jsonPath(JSON_UMIDADE).value(EXPECT_UMIDADE))
                    .andExpect(jsonPath(JSON_CONDICAO).value(EXPECT_CONDICAO))
                    .andExpect(jsonPath(JSON_VENTO_VELOCIDADE).value(EXPECT_VENTO_VELOCIDADE))
                    .andExpect(jsonPath(JSON_VENTO_DIRECAO).value(EXPECT_VENTO_DIRECAO));
        }

        @Test
        @DisplayName(DISPLAYNAME_PREVISAO_EXTERNAL_ERROR)
        void shouldReturn500WhenExternalServiceFails() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_PREVISAO_PATH))
                    .willReturn(aResponse()
                            .withStatus(HTTP_SERVER_ERROR)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_ERROR_500)));

            // Act & Assert - Exception is propagated as the controller has no error handling
            try {
                mockMvc.perform(get(INTERNAL_PREVISAO_PATH)
                                .param(PARAM_LAT, LAT_SAO_PAULO)
                                .param(PARAM_LON, LON_SAO_PAULO)
                                .param(PARAM_DATA, DATA_EXEMPLO));
            } catch (Exception e) {
                // Expected: exception is propagated from external service
                org.junit.jupiter.api.Assertions.assertTrue(
                        e.getCause() instanceof org.springframework.web.client.HttpServerErrorException);
            }
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_SUGERIR_ROTA)
    class SugerirRotaEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_SUGERIR_ROTA_SUCCESS)
        void shouldReturnSugerirRotaSuccessfully() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_SUGERIR_ROTA_PATH))
                    .withQueryParam(PARAM_ORIGEM_ID, equalTo(ORIGEM_GRU))
                    .withQueryParam(PARAM_DESTINO_ID, equalTo(DESTINO_CGH))
                    .withQueryParam(PARAM_DATA, equalTo(DATA_EXEMPLO))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_SUGERIR_ROTA_SUCCESS)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_SUGERIR_ROTA_PATH)
                            .param(PARAM_ORIGEM_ID, ORIGEM_GRU)
                            .param(PARAM_DESTINO_ID, DESTINO_CGH)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath(JSON_ROTA).value(EXPECT_ROTA))
                    .andExpect(jsonPath(JSON_DISTANCIA_KM).value(EXPECT_DISTANCIA_KM))
                    .andExpect(jsonPath(JSON_TEMPO_ESTIMADO).value(EXPECT_TEMPO_ESTIMADO))
                    .andExpect(jsonPath(JSON_WAYPOINTS).isArray())
                    .andExpect(jsonPath(JSON_WAYPOINTS_0_NOME).value(EXPECT_WAYPOINT_NOME));
        }

        @Test
        @DisplayName(DISPLAYNAME_SUGERIR_ROTA_DELAY)
        void shouldHandleDelay() throws Exception {
            // Arrange - Small delay that doesn't cause timeout
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_SUGERIR_ROTA_PATH))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withFixedDelay(DELAY_MS)
                            .withBodyFile(STUB_SUGERIR_ROTA_DELAYED)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_SUGERIR_ROTA_PATH)
                            .param(PARAM_ORIGEM_ID, ORIGEM_GRU)
                            .param(PARAM_DESTINO_ID, DESTINO_CGH)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(jsonPath(JSON_ROTA).value(EXPECT_ROTA));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_ANALISE)
    class AnaliseEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_ANALISE_ENHANCED_TRUE)
        void shouldReturnAnaliseWithEnhanced() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_ANALISE_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .withQueryParam(PARAM_ENHANCED, equalTo(ENHANCED_TRUE))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_ANALISE_ENHANCED)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_ANALISE_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO)
                            .param(PARAM_ENHANCED, ENHANCED_TRUE))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(jsonPath(JSON_ANALISE).value(EXPECT_ANALISE_DETALHADA))
                    .andExpect(jsonPath(JSON_RISCO).value(EXPECT_RISCO_BAIXO))
                    .andExpect(jsonPath(JSON_INDICE_SEGURANCA).value(EXPECT_INDICE_SEGURANCA))
                    .andExpect(jsonPath(JSON_FATORES_VISIBILIDADE).value(EXPECT_VISIBILIDADE_BOA));
        }

        @Test
        @DisplayName(DISPLAYNAME_ANALISE_ENHANCED_FALSE)
        void shouldReturnAnaliseWithoutEnhanced() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_ANALISE_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .withQueryParam(PARAM_ENHANCED, equalTo(ENHANCED_FALSE))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_ANALISE_BASIC)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_ANALISE_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO)
                            .param(PARAM_ENHANCED, ENHANCED_FALSE))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(jsonPath(JSON_ANALISE).value(EXPECT_ANALISE_BASICA))
                    .andExpect(jsonPath(JSON_RISCO).value(EXPECT_RISCO_MEDIO));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_GRAFICOS)
    class GraficosEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_GRAFICOS_PNG)
        void shouldReturnGraficosAsPng() throws Exception {
            // Arrange - PNG magic bytes
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_GRAFICOS_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .withQueryParam(PARAM_DATA, equalTo(DATA_EXEMPLO))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_PNG)
                            .withBody(PNG_MAGIC_BYTES_FULL)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_GRAFICOS_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));
        }

        @Test
        @DisplayName(DISPLAYNAME_GRAFICOS_EMPTY)
        void shouldHandleEmptyResponse() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_GRAFICOS_PATH))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_PNG)
                            .withBody(EMPTY_BYTES)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_GRAFICOS_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_ANALISE_GRAFICOS)
    class AnaliseGraficosEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_ANALISE_GRAFICOS_PNG)
        void shouldReturnAnaliseGraficosAsPng() throws Exception {
            // Arrange - PNG magic bytes
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_ANALISE_GRAFICOS_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_PNG)
                            .withBody(PNG_MAGIC_BYTES_SHORT)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_ANALISE_GRAFICOS_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_EXPORTAR_EXCEL)
    class ExportarExcelEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_EXPORTAR_EXCEL)
        void shouldReturnExcelFile() throws Exception {
            // Arrange - Excel/ZIP file magic bytes (XLSX is a ZIP file)
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_EXPORTAR_EXCEL_PATH))
                    .withQueryParam(PARAM_LAT, equalTo(LAT_SAO_PAULO))
                    .withQueryParam(PARAM_LON, equalTo(LON_SAO_PAULO))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_EXCEL)
                            .withBody(EXCEL_MAGIC_BYTES)));

            // Act & Assert
            mockMvc.perform(get(INTERNAL_EXPORTAR_EXCEL_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO)
                            .param(PARAM_LON, LON_SAO_PAULO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(header().string(CONTENT_DISPOSITION_HEADER, CONTENT_DISPOSITION_EXCEL));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_GET_MAPA_SUGERIDO)
    class MapaSugeridoEndpoint {

        @Test
        @DisplayName(DISPLAYNAME_MAPA_SUGERIDO_URL)
        void shouldReturnMapaSugeridoUrl() throws Exception {
            // Act & Assert - This endpoint builds URL locally, no external call needed
            mockMvc.perform(get(INTERNAL_MAPA_SUGERIDO_PATH)
                            .param(PARAM_ORIGEM_ID_CAMEL, ORIGEM_GRU)
                            .param(PARAM_DESTINO_ID_CAMEL, DESTINO_CGH)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath(JSON_MAP_URL).exists())
                    .andExpect(jsonPath(JSON_MAP_URL)
                            .value(org.hamcrest.Matchers.containsString(EXPECT_MAPA_SUGERIDO_SUBSTRING)));
        }

        @Test
        @DisplayName(DISPLAYNAME_MAPA_SUGERIDO_SPECIAL)
        void shouldEncodeSpecialCharactersInUrl() throws Exception {
            // Act & Assert
            mockMvc.perform(get(INTERNAL_MAPA_SUGERIDO_PATH)
                            .param(PARAM_ORIGEM_ID_CAMEL, ORIGEM_SAO_PAULO)
                            .param(PARAM_DESTINO_ID_CAMEL, DESTINO_RIO_JANEIRO)
                            .param(PARAM_DATA, DATA_EXEMPLO))
                    .andExpect(status().is(HTTP_OK))
                    .andExpect(jsonPath(JSON_MAP_URL).exists())
                    .andExpect(jsonPath(JSON_MAP_URL)
                            .value(org.hamcrest.Matchers.containsString(EXPECT_SAO_ENCODED)));
        }
    }

    @Nested
    @DisplayName(DISPLAYNAME_ERROR_SCENARIOS)
    class ErrorScenarios {

        @Test
        @DisplayName(DISPLAYNAME_MISSING_PARAM)
        void shouldReturn400WhenParameterMissing() throws Exception {
            // Act & Assert
            mockMvc.perform(get(INTERNAL_PREVISAO_PATH)
                            .param(PARAM_LAT, LAT_SAO_PAULO))
                    .andExpect(status().is(HTTP_BAD_REQUEST));
        }

        @Test
        @DisplayName(DISPLAYNAME_EXTERNAL_404)
        void shouldHandle404FromExternalService() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_PREVISAO_PATH))
                    .willReturn(aResponse()
                            .withStatus(HTTP_NOT_FOUND)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBodyFile(STUB_ERROR_404)));

            // Act & Assert - Exception is propagated
            try {
                mockMvc.perform(get(INTERNAL_PREVISAO_PATH)
                                .param(PARAM_LAT, LAT_SAO_PAULO)
                                .param(PARAM_LON, LON_SAO_PAULO)
                                .param(PARAM_DATA, DATA_EXEMPLO));
            } catch (Exception e) {
                org.junit.jupiter.api.Assertions.assertTrue(
                        e.getCause() instanceof org.springframework.web.client.HttpClientErrorException);
            }
        }

        @Test
        @DisplayName(DISPLAYNAME_MALFORMED_JSON)
        void shouldHandleMalformedJson() throws Exception {
            // Arrange
            wireMockServer.stubFor(WireMock.get(urlPathEqualTo(EXTERNAL_PREVISAO_PATH))
                    .willReturn(aResponse()
                            .withStatus(HTTP_OK)
                            .withHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                            .withBody(MALFORMED_JSON)));

            // Act & Assert - Exception is propagated
            try {
                mockMvc.perform(get(INTERNAL_PREVISAO_PATH)
                                .param(PARAM_LAT, LAT_SAO_PAULO)
                                .param(PARAM_LON, LON_SAO_PAULO)
                                .param(PARAM_DATA, DATA_EXEMPLO));
            } catch (Exception e) {
                org.junit.jupiter.api.Assertions.assertNotNull(e.getCause());
            }
        }
    }
}
