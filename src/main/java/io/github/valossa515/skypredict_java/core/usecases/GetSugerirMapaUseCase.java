package io.github.valossa515.skypredict_java.core.usecases;

import io.github.valossa515.skypredict_java.core.domain.MapaSugeridoUrlResponse;

public interface GetSugerirMapaUseCase {
    MapaSugeridoUrlResponse execute(String origemId, String destinoId, String data);
}
