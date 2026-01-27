# SkyPredict Java

Java API that proxies requests to the SkyPredict Python service and exposes REST endpoints for forecasts, analysis, charts, and Excel export.

## Requirements

- Java 25
- Gradle (or use the included Gradle wrapper)
- SkyPredict Python service running (default: http://localhost:5000)

## Configuration

Main settings are in `src/main/resources/application.yaml`.

- Python base URL: `skypredict.python.base-url`
- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

## Run locally

Using Gradle wrapper:

```
./gradlew bootRun
```

Or build a jar and run:

```
./gradlew build
java -jar build/libs/skypredict-java-0.0.1-SNAPSHOT.jar
```

The application starts on port `8080` by default.

## API Endpoints

Base path: `/java`

- `GET /java/previsao?lat={lat}&lon={lon}&data={data}`
- `GET /java/sugerir_rota?origem_id={id}&destino_id={id}&data={data}`
- `GET /java/analise?lat={lat}&lon={lon}`
- `GET /java/graficos?lat={lat}&lon={lon}&data={data}` (PNG)
- `GET /java/analise/graficos?lat={lat}&lon={lon}` (PNG)
- `GET /java/exportar_excel?lat={lat}&lon={lon}` (XLSX)

## Swagger

After starting the app:

- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Notes

- The Java API proxies requests to the Python service configured in `application.yaml`.
- If you change the Python service address, restart the app.
