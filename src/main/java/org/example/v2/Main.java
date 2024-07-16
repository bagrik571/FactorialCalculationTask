package org.example.v2;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Result> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Result> outputQueue = new LinkedBlockingQueue<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter thread count: ");
        int threadCount = scanner.nextInt();
        scanner.close();

// Step 1: Read numbers from file and add to queue
        logger.log(Level.INFO, "Start reading file...");
        File inputFile = new File("C:\\Users\\bagri\\input.txt");
        ExecutorService readExecutor = Executors.newSingleThreadExecutor();
        readExecutor.submit(new ReadFile(inputQueue, inputFile));

// Step 2: Compute factorials using a pool of threads
        logger.log(Level.INFO, "Start calculating factorial for elements in queue...");
        AtomicInteger throttleCounter = new AtomicInteger(0);
        ScheduledExecutorService throttler = Executors.newScheduledThreadPool(1);
        throttler.scheduleAtFixedRate(() -> throttleCounter.set(0), 0, 1, TimeUnit.SECONDS);
        ExecutorService computeExecutor = Executors.newFixedThreadPool(threadCount);

        Factorial factorial = new Factorial();
        for (int i = 0; i < threadCount; i++) {
            computeExecutor.execute(new Calculate(inputQueue, outputQueue, throttleCounter, factorial));
        }

// Step 3: Write results to file
        logger.log(Level.INFO, "Start writing values to file...");
        File outputFile = new File("C:\\Users\\bagri\\output.txt");
        ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
        writeExecutor.submit(new WriteFile(outputQueue, outputFile));

        readExecutor.shutdown();
        readExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        for (int i = 0; i < threadCount; i++) {
            inputQueue.put(new Result(i, Integer.MIN_VALUE, null)); // Signal to stop computation threads
        }
        computeExecutor.shutdown();
        computeExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        outputQueue.put(new Result(-1, Integer.MIN_VALUE, null)); // Signal to stop writing thread
        writeExecutor.shutdown();
        writeExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        throttler.shutdown();
        throttler.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        logger.log(Level.INFO, "File saved!");
    }
}