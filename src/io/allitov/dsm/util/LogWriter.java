package io.allitov.dsm.util;

import io.allitov.dsm.model.Fraction;

import java.util.Map;

@SuppressWarnings("java:S106")
public final class LogWriter {

    private LogWriter() {
        throw new IllegalStateException("Utility class");
    }

    public static void printHeader(String message) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + message);
        System.out.println("=".repeat(50));
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printTable(Fraction[][] table, int[] basis, Map<Integer, Fraction> ratios) {
        int rows = table.length;
        int cols = table[0].length;
        int colWidth = 12;

        System.out.print(" Базис |");
        for (int j = 0; j < cols - 1; j++) {
            System.out.printf("%" + colWidth + "s", "x" + (j + 1));
        }
        System.out.printf("|%" + colWidth + "s", "1");
        System.out.println();
        System.out.println("-".repeat(8 + colWidth * cols + 2));

        for (int i = 0; i < rows - 1; i++) {
            String bVar = (basis[i] != -1) ? "  x" + (basis[i] + 1) : "  --";
            System.out.printf("%-7s|", bVar);

            for (int j = 0; j < cols; j++) {
                if (j == cols - 1) System.out.print("|");
                System.out.printf("%" + colWidth + "s", table[i][j].toString());
            }
            System.out.println();
        }

        System.out.println("-".repeat(8 + colWidth * cols + 2));
        System.out.printf("%-7s|", "  Z");
        for (int j = 0; j < cols; j++) {
            if (j == cols - 1) System.out.print("|");
            System.out.printf("%" + colWidth + "s", table[rows - 1][j].toString());
        }
        System.out.println();

        if (ratios != null && !ratios.isEmpty()) {
            System.out.printf("%-7s|", "  CO");
            for (int j = 0; j < cols - 1; j++) {
                if (ratios.containsKey(j)) {
                    System.out.printf("%" + colWidth + "s", ratios.get(j).toString());
                } else {
                    System.out.printf("%" + colWidth + "s", " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printResult(String label, Fraction[][] table, int[] basis) {
        int rows = table.length - 1;
        int cols = table[0].length - 1;
        Fraction[] result = new Fraction[cols];
        for (int i = 0; i < cols; i++) result[i] = Fraction.of(0);

        for (int i = 0; i < rows; i++) {
            if (basis[i] != -1 && basis[i] < cols) {
                result[basis[i]] = table[i][cols];
            }
        }

        IO.println(label + ":");
        for (int i = 0; i < result.length; i++) {
            IO.println("x" + (i + 1) + " = " + result[i]);
        }
        IO.println("Z = " + table[rows][cols]);
        IO.println("-------------------------");
    }
}
