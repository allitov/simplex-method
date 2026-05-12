package io.allitov.dsm.util;

import io.allitov.dsm.model.Fraction;
import io.allitov.dsm.model.Problem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Утильный класс для чтения задачи из файла.
 */
public final class ProblemReader {

    private ProblemReader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Прочитать задачу линейного программирования из файла.
     *
     * @param fileName путь к файлу на чтение.
     * @return проблема линейного программирования.
     * @throws IOException если произошла ошибка во время чтения файла.
     */
    public static Problem readProblemFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        String[][] rawData = lines.stream()
                .map(line -> line.strip().split("\\s+"))
                .toArray(String[][]::new);

        return createProblem(rawData);
    }

    private static Problem createProblem(String[][] rawData) {
        int rows = rawData.length;
        int cols = rawData[0].length;

        Fraction[][] constraints = new Fraction[rows - 1][cols];
        Fraction[] targetFunction = new Fraction[cols];
        for (int row = 0; row < rows - 1; row++) {
            for (int col = 0; col < cols; col++) {
                constraints[row][col] = parseFraction(rawData[row][col]);
            }
        }
        for (int col = 0; col < cols; col++) {
            targetFunction[col] = parseFraction(rawData[rows - 1][col]);
        }

        return new Problem(constraints, targetFunction);
    }

    private static Fraction parseFraction(String rawValue) {
        return Fraction.of(Long.parseLong(rawValue));
    }
}
