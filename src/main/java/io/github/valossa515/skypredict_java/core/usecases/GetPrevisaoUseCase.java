package io.github.valossa515.skypredict_java.core.usecases;


import java.util.Map;

public interface GetPrevisaoUseCase {
    Map<String, Object> execute(double lat, double lon, String data);
}
