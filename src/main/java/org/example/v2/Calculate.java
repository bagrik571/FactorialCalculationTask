package org.example.v2;

import com.sun.jdi.IntegerType;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculate implements Runnable {
    private final BlockingQueue<Result> inputQueue;
    private final BlockingQueue<Result> outputQueue;
    private final AtomicInteger throttleCounter;
    private final Factorial factorial;
    private static final Logger logger = Logger.getLogger(Calculate.class.getName());

    public Calculate(BlockingQueue<Result> inputQueue, BlockingQueue<Result> outputQueue, AtomicInteger throttleCounter, Factorial factorial) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.throttleCounter = throttleCounter;
        this.factorial = factorial;
    }

    @Override
    public void run() {
        try {
            while (true) {
//                Result inputResult = inputQueue.take();
//                if (inputResult.getValue().equals(Integer.MIN_VALUE)) {
//                    inputQueue.put(new Result(inputResult.getIndex(), Integer.MIN_VALUE, null)); // Re insert the termination signal for other threads
//                    break;
//                }
                    Result inputResult = inputQueue.take();
                    if (inputResult.getIndex() == -1) {
                        inputQueue.put(new Result(inputResult.getIndex(), inputResult.getValue(), null)); // Re-insert the termination signal for other threads
                        break;
                    }
                if(!inputResult.getValue().equals("not valid value")) {
                    while (throttleCounter.getAndIncrement() >= 100) {
                        Thread.sleep(10); // Throttle if over 100 calculations per second
                    }
                    Integer value;
                    try {
                        value = Integer.parseInt(inputResult.getValue());
                    } catch (NumberFormatException e) {
                        outputQueue.put(new Result(inputResult.getIndex(), "not valid value", null));
                        continue;
                    }
                    BigInteger result = factorial.calculateFactorial(value);
                    outputQueue.put(new Result(inputResult.getIndex(), inputResult.getValue(), result));
                } else {
                    outputQueue.put(new Result(inputResult.getIndex(), inputResult.getValue(), null));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Thread interrupted", e);
        }
    }
}
