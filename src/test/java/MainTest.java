package org.example.v2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainTest {
    private static final Logger logger = Logger.getLogger(MainTest.class.getName());

    public static void main(String[] args) throws InterruptedException, IOException {
        Main.main(args); // start main

        File inputFile = new File("C:\\Users\\bagri\\input_test.txt");
        File outputFile = new File("C:\\Users\\bagri\\output_test.txt");

        BlockingQueue<Result> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Result> outputQueue = new LinkedBlockingQueue<>();

        readQueue(inputQueue, inputFile, true);
        readQueue(outputQueue, outputFile, false);

        compareQueues(inputQueue, outputQueue);
    }

    private static void readQueue(BlockingQueue<Result> queue, File file, boolean isInputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (isInputFile) {
                    if (!line.isEmpty() && line.matches("\\d+")) {
                        queue.add(new Result(index++, line, null));
                    } else {
                        queue.add(new Result(index++, "not valid value", null));
                    }
                } else {
                    String[] parts = line.split(" = ");
                    if (parts.length == 2) {
                        queue.add(new Result(index++, parts[0], null));
                    } else {
                        queue.add(new Result(index++, "not valid value", null));
                    }
                }
            }
        }
    }

    private static void compareQueues(BlockingQueue<Result> inputQueue, BlockingQueue<Result> outputQueue) {
        boolean match = true;
        int inputQueueSize = inputQueue.size();
        int outputQueueSize = outputQueue.size();

        if (inputQueueSize != outputQueueSize) {
            logger.log(Level.SEVERE, "Numbers of elements in queues doesnt match: Input queue = " + inputQueueSize + ", Output queue = " + outputQueueSize);
            match = false;
        }

        while (!inputQueue.isEmpty() && !outputQueue.isEmpty()) {
            Result inputResult = inputQueue.poll();
            Result outputResult = outputQueue.poll();

            if (inputResult != null && outputResult != null) {
                if (!inputResult.getValue().equals(outputResult.getValue())) {
                    logger.log(Level.SEVERE, "Values dont match for index " + inputResult.getIndex() + ": " + inputResult.getValue() + " != " + outputResult.getValue());
                    match = false;
                }
            } else {
                match = false;
                logger.log(Level.SEVERE, "null in queues");
            }
        }

        if (match) {
            logger.log(Level.INFO, "All rows are match");
        }
    }
}