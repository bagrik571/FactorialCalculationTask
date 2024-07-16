package org.example.v2;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class Factorial {
    private final ConcurrentHashMap<Integer, BigInteger> cache = new ConcurrentHashMap<>();

    public BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Input must be a non-negative integer.");
        }
        return cache.computeIfAbsent(n, this::calculateFactorial);
    }

    public BigInteger calculateFactorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
