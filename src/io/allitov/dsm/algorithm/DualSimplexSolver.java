package io.allitov.dsm.algorithm;

import io.allitov.dsm.model.Fraction;
import io.allitov.dsm.model.Problem;
import io.allitov.dsm.util.LogWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация двойственного симплекс-метода.
 */
public class DualSimplexSolver {

    /**
     * Решить задачу линейного программирования.
     *
     * @param problem задача, которую нужно решить.
     */
    public void solve(Problem problem) {
        int[] basis = findBasis(problem.constraints());
        LogWriter.printMessage("Базис: " + Arrays.toString(Arrays.stream(basis).map(i -> i + 1).toArray()));

        Fraction[][] table = prepareTable(problem, basis);
        LogWriter.printMessage("Начальная симплекс-таблица:");
        LogWriter.printTable(table, basis, null);

        if (!isDualFeasible(table)) {
            LogWriter.printMessage("Ошибка: Задача не является двойственно допустимой.");
            return;
        }

        LogWriter.printMessage("Задача двойственно допустимая.");

        int iteration = 1;
        while (hasNegativeB(table)) {
            LogWriter.printHeader("Таблица №" + iteration++);

            int pivotRow = findPivotRow(table);
            Map<Integer, Fraction> ratios = new HashMap<>();
            int zRow = table.length - 1;
            for (int j = 0; j < table[0].length - 1; j++) {
                if (table[pivotRow][j].isNegative()) {
                    ratios.put(j, table[zRow][j].divide(table[pivotRow][j]));
                }
            }
            LogWriter.printTable(table, basis, ratios);

            int pivotCol = findPivotCol(table, pivotRow);

            if (pivotCol == -1) {
                LogWriter.printMessage("Задача не имеет допустимых решений.");
                return;
            }

            LogWriter.printMessage("Разрешающий элемент: [" + (pivotRow + 1) + ", " + (pivotCol + 1) + "]");
            LogWriter.printMessage("В базис входит x" + (pivotCol + 1) + " вместо x" + (basis[pivotRow] + 1));

            table = gaussStep(table, pivotRow, pivotCol);
            basis[pivotRow] = pivotCol;
        }
        LogWriter.printHeader("Таблица №" + iteration);
        LogWriter.printTable(table, basis, null);
        LogWriter.printMessage("Оптимальное решение найдено.");
        LogWriter.printResult("Результат", table, basis);

        checkForAlternativeSolutions(table, basis);
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

    private boolean isDualFeasible(Fraction[][] table) {
        int zRowIndex = table.length - 1;
        int cols = table[0].length;

        for (int col = 0; col < cols; col++) {
            if (table[zRowIndex][col].isNegative()) {
                return false;
            }
        }

        return true;
    }

    private boolean hasNegativeB(Fraction[][] table) {
        int rows = table.length - 1;
        int bColIndex = table[0].length - 1;

        for (int row = 0; row < rows; row++) {
            if (table[row][bColIndex].isNegative()) {
                return true;
            }
        }

        return false;
    }

    private int findPivotRow(Fraction[][] table) {
        int rows = table.length - 1;
        int bColIndex = table[0].length - 1;
        int pivotRow = -1;
        Fraction minB = Fraction.of(0);

        for (int row = 0; row < rows; row++) {
            Fraction bValue = table[row][bColIndex];
            if (bValue.isNegative() && bValue.absCompareTo(minB) > 0) {
                minB = bValue;
                pivotRow = row;
            }
        }

        return pivotRow;
    }

    private int findPivotCol(Fraction[][] table, int pivotRow) {
        int zRowIndex = table.length - 1;
        int cols = table[0].length - 1;
        int pivotCol = -1;
        Fraction minRatio = null;
        for (int col = 0; col < cols; col++) {
            Fraction rowValue = table[pivotRow][col];
            if (!rowValue.isNegative()) {
                continue;
            }
            Fraction zValue = table[zRowIndex][col];
            Fraction ratio = zValue.divide(rowValue);
            if (pivotCol == -1 || ratio.absCompareTo(minRatio) < 0) {
                minRatio = ratio;
                pivotCol = col;
            }
        }

        return pivotCol;
    }

    private void checkForAlternativeSolutions(Fraction[][] table, int[] basis) {
        int zRowIndex = table.length - 1;
        int cols = table[0].length - 1;

        for (int col = 0; col < cols; col++) {
            boolean isBasis = false;
            for (int b : basis) {
                if (b == col) {
                    isBasis = true;
                    break;
                }
            }
            if (!isBasis && table[zRowIndex][col].isPositive()) {
                LogWriter.printMessage("Обнаружено бесконечное множество решений. Поиск альтернативного.");
                int pivotRow = -1;
                for (int i = 0; i < table.length - 1; i++) {
                    if (table[i][col].isPositive()) {
                        pivotRow = i;
                        break;
                    }
                }

                if (pivotRow != -1) {
                    Fraction[][] altTable = gaussStep(table, pivotRow, col);
                    int[] altBasis = basis.clone();
                    altBasis[pivotRow] = col;
                    LogWriter.printResult("Решение 2 (альтернативное)", altTable, altBasis);
                    return;
                } else {
                    LogWriter.printMessage("Область допустимых решений не ограничена.");
                }
            }
        }
    }
}
