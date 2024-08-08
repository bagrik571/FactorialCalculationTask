package org.example.v2;

import java.math.BigInteger;

public class Result implements Comparable<Result> {
    private final Integer index;
    private final String value;
    private final BigInteger factorial;

    public Result(Integer index, String value, BigInteger factorial) {
        this.index = index;
        this.value = value;
        this.factorial = factorial;
    }

    public Integer getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public BigInteger getFactorial() {
        return factorial;
    }

    @Override
    public String toString() {
        return value + " = " + factorial;
    }

    @Override
    public int compareTo(Result other) {
        return this.index.compareTo(other.index);
    }
}