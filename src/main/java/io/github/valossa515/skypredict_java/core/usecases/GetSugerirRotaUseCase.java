package io.github.valossa515.skypredict_java.core.usecases;

import java.util.Map;

public interface GetSugerirRotaUseCase {
    Map<String, Object> execute(String origemId, String destinoId, String data);
}
