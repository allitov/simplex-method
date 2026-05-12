package io.allitov.dsm.algorithm;

import io.allitov.dsm.model.Fraction;
import io.allitov.dsm.model.Problem;

import java.util.Arrays;

public class DualSimplexSolver {

    public void solve(Problem problem) {
        // todo: найти базисные переменные
        int[] basis = findBasis(problem.constraints());
        IO.println("Basis: " + Arrays.toString(basis));
        // todo: получить таблицу с функцией через базисные переменные.
        // todo: проверить применимость метода
        // todo: найти ведущий элемент
        // todo: выполнить переход
        // todo: проверить, не решение ли это
    }

    private int[] findBasis(Fraction[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length - 1;

        int[] basis = new int[rows];
        Arrays.fill(basis, -1);
        for (int col = 0; col < cols; col++) {
            int basisRow = -1;
            boolean found = true;
            for (int row = 0; row < rows; row++) {
                if (matrix[row][col].isOne() && basisRow == -1) {
                    basisRow = row;
                } else if (!matrix[row][col].isZero()) {
                    found = false;
                    break;
                }
            }
            if (found) {
                basis[basisRow] = col;
            }
        }

        if (Arrays.stream(basis).anyMatch(b -> b == -1)) {
            throw new IllegalArgumentException("No basis found!");
        }

        return basis;
    }
}
