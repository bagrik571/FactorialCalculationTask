package org.example.v2;

import java.math.BigInteger;

public class Result implements Comparable<Result> {
    private final Integer index;
    private final Integer value;
    private final BigInteger factorial;

    public Result(Integer index, Integer value, BigInteger factorial) {
        this.index = index;
        this.value = value;
        this.factorial = factorial;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getValue() {
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