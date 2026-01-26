package io.github.valossa515.skypredict_java.core.usecases;

public interface GetGraficosUseCase {
    byte[] execute(double lat, double lon, String data);
}
