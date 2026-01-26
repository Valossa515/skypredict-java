package io.github.valossa515.skypredict_java.core.usecases;

public interface ExportarExcelUseCase {
    byte[] execute(double lat, double lon);
}
