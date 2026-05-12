package io.allitov.dsm.algorithm;

import io.allitov.dsm.model.Fraction;
import io.allitov.dsm.model.Problem;

import java.util.Arrays;

public class DualSimplexSolver {

    public void solve(Problem problem) {
        int[] basis = findBasis(problem.constraints());
        IO.println("Basis: " + Arrays.toString(basis));

        Fraction[][] table = prepareTable(problem, basis);
        IO.println("Table: " + Arrays.deepToString(table));
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

    private Fraction[][] prepareTable(Problem problem, int[] basis) {
        int rows = problem.constraints().length;
        int cols = problem.constraints()[0].length;

        Fraction[][] table = new Fraction[rows + 1][cols];
        for (int row = 0; row < rows; row++) {
            System.arraycopy(problem.constraints()[row], 0, table[row], 0, cols);
        }
        for (int col = 0; col < cols; col++) {
            if (col < cols - 1) {
                table[rows][col] = problem.targetFunction()[col].negate();
            } else {
                table[rows][col] = problem.targetFunction()[col];
            }
        }

        for (int basisRow = 0; basisRow < basis.length; basisRow++) {
            table = gaussStep(table, basisRow, basis[basisRow]);
        }

        return table;
    }

    private Fraction[][] gaussStep(Fraction[][] table, int row, int col) {
        int rows = table.length;
        int cols = table[0].length;
        Fraction[][] nextTable = new Fraction[rows][cols];
        Fraction pivotElement = table[row][col];

        for (int j = 0; j < cols; j++) {
            nextTable[row][j] = table[row][j].divide(pivotElement);
        }

        for (int i = 0; i < rows; i++) {
            if (i == row) {
                continue;
            }
            Fraction multiplier = table[i][col];
            for (int j = 0; j < cols; j++) {
                nextTable[i][j] = table[i][j].subtract(multiplier.multiply(nextTable[row][j]));
            }
        }
        return nextTable;
    }
}
