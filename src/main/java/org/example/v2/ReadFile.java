package org.example.v2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadFile implements Runnable {
    private final BlockingQueue<Result> queue;
    private final File inputFile;
    private static final Logger logger = Logger.getLogger(ReadFile.class.getName());

    public ReadFile(BlockingQueue<Result> queue, File inputFile) {
        this.queue = queue;
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.matches("\\d+")) {
                    try {
                        //Integer value = Integer.getInteger(line);
                        queue.put(new Result(index++, line, null));
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Invalid number format: " + line);
                    }
                }else{
                    queue.put(new Result(index++, "not valid value", null));
                }
            }
            logger.log(Level.INFO, "Queue size after reading file: " + queue.size());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error reading file", e);
        }
    }
}
