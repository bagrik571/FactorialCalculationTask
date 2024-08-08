package org.example.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFile implements Runnable {
    private final BlockingQueue<Result> queue;
    private final File outputFile;
    private static final Logger logger = Logger.getLogger(WriteFile.class.getName());
    private int lineCount = 0;

    public WriteFile(BlockingQueue<Result> queue, File outputFile) {
        this.queue = queue;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creating output file", e);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            PriorityQueue<Result> sortedResults = new PriorityQueue<>();
            while (true) {
                Result result = queue.take();
                if (result.getIndex() == -1) {
                    break;
                }
                sortedResults.add(result);
            }
            while (!sortedResults.isEmpty()) {
                Result result = sortedResults.poll();
                writer.write(result.getValue() + " = " + result.getFactorial());
                writer.newLine();
                lineCount++;
            }
            logger.log(Level.INFO, "Total lines written: " + lineCount);
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error writing to file", e);
        }
    }
}
