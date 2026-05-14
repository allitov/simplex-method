package io.allitov.dsm.model;

import java.math.BigInteger;

/**
 * Реализация простых дробей.
 * <br>
 * Объекты класса иммутабельные. Для создания объектов нужно использовать фабричные методы {@code of()}.
 */
public class Fraction {

    private final BigInteger numerator;
    private final BigInteger denominator;

    private Fraction(BigInteger num, BigInteger den) {
        this.numerator = num;
        this.denominator = den;
    }

    /**
     * Фабричный метод для создания простой дроби.
     *
     * @param numerator   числитель.
     * @param denominator знаменатель.
     * @return новая простая дробь.
     * @throws ArithmeticException если знаменатель равен нулю.
     */
    public static Fraction of(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator cannot be zero");
        }

        BigInteger sign = denominator.signum() == -1 ? BigInteger.valueOf(-1) : BigInteger.ONE;
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd).multiply(sign);
        denominator = denominator.divide(gcd).abs();
        return new Fraction(numerator, denominator);
    }

    /**
     * Фабричный метод для создания простой дроби.
     *
     * @param numerator числитель.
     * @return новая простая дробь.
     */
    public static Fraction of(long numerator) {
        return of(BigInteger.valueOf(numerator), BigInteger.ONE);
    }

    /**
     * Сложить две простых дроби.
     *
     * @param other простая дробь, которую нужно добавить к текущей.
     * @return простая дробь после сложения.
     */
    public Fraction add(Fraction other) {
        BigInteger newDenominator = this.denominator.multiply(other.denominator);
        BigInteger num1 = this.numerator.multiply(other.denominator);
        BigInteger num2 = other.numerator.multiply(this.denominator);
        BigInteger newNumerator = num1.add(num2);

        return Fraction.of(newNumerator, newDenominator);
    }

    /**
     * Вычесть простые дроби.
     *
     * @param other простая дробь, которую нужно отнять от текущей.
     * @return простая дробь после вычитания.
     */
    public Fraction subtract(Fraction other) {
        BigInteger newDenominator = this.denominator.multiply(other.denominator);
        BigInteger num1 = this.numerator.multiply(other.denominator);
        BigInteger num2 = other.numerator.multiply(this.denominator);
        BigInteger newNumerator = num1.subtract(num2);

        return Fraction.of(newNumerator, newDenominator);
    }

    /**
     * Умножить простые дроби.
     *
     * @param other простая дробь, на которую нужно умножить текущую.
     * @return простая дробь после умножения.
     */
    public Fraction multiply(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.numerator);
        BigInteger newDenominator = this.denominator.multiply(other.denominator);

        return Fraction.of(newNumerator, newDenominator);
    }

    /**
     * Поделить простые дроби.
     *
     * @param other простая дробь, на которую нужно поделить текущую.
     * @return простая дробь после деления.
     */
    public Fraction divide(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.denominator);
        BigInteger newDenominator = this.denominator.multiply(other.numerator);

        return Fraction.of(newNumerator, newDenominator);
    }

    /**
     * Вернуть противоположное по знаку значение дроби.
     *
     * @return противоположная по знаку простая дробь.
     */
    public Fraction negate() {
        return Fraction.of(this.numerator.negate(), this.denominator);
    }

    /**
     * Сравнить дроби по модулю.
     *
     * @param other простая дробь, с которой нужно сравнить текущую.
     * @return -1, 0 или 1 если текущая простая дробь меньше, равна или больше по модулю чем переданная соответственно.
     */
    public int absCompareTo(Fraction other) {
        BigInteger thisNum = this.numerator.multiply(other.denominator).abs();
        BigInteger otherNum = other.numerator.multiply(this.denominator).abs();

        return thisNum.compareTo(otherNum);
    }

    /**
     * Проверить, что простая дробь равна нулю.
     *
     * @return {@code true} если числитель равен нулю; иначе {@code false}.
     */
    public boolean isZero() {
        return numerator.equals(BigInteger.ZERO);
    }

    /**
     * Проверить, что простая дробь равна единице.
     *
     * @return {@code true} если числитель и знаменатель равны единице; иначе {@code false}.
     */
    public boolean isOne() {
        return numerator.equals(BigInteger.ONE) && denominator.equals(BigInteger.ONE);
    }

    /**
     * Проверить, что простая дробь отрицательная.
     *
     * @return {@code true} если числитель отрицательный; иначе {@code false}.
     */
    public boolean isNegative() {
        return numerator.signum() == -1;
    }

    /**
     * Проверить, что простая дробь положительная.
     *
     * @return {@code true} если числитель положительный; иначе {@code false}.
     */
    public boolean isPositive() {
        return numerator.signum() == 1;
    }

    @Override
    public String toString() {
        return denominator.equals(BigInteger.ONE) ? numerator.toString() : numerator + "/" + denominator;
    }

}
