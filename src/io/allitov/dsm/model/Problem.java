package io.allitov.dsm.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Представление задачи линейного программирования.
 *
 * @param constraints    Матрица коэффициентов и свободных членов.
 * @param targetFunction Коэффициенты целевой функции.
 */
public record Problem(
        Fraction[][] constraints,
        Fraction[] targetFunction
) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Problem problem = (Problem) o;

        return Objects.deepEquals(constraints, problem.constraints) && Objects.deepEquals(targetFunction, problem.targetFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(constraints), Arrays.hashCode(targetFunction));
    }

    @Override
    public String toString() {
        return "Problem{" +
                "constraints=" + Arrays.deepToString(constraints) +
                ", targetFunction=" + Arrays.toString(targetFunction) +
                '}';
    }
}
