package com.app.main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import com.app.aggregator.Aggregator;
import com.app.constants.Constants;
import com.app.consumer.MatcherWorker;
import com.app.producer.ChunckProducer;

public class MatchFinderApp {

    public static void main(String[] args) throws IOException, InterruptedException {

    	BlockingQueue<Map<Integer, String>> queue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_COUNT);
        Aggregator aggregator = new Aggregator();

        // Producer: Read file in chunks
        Thread producer = new Thread(new ChunckProducer(Constants.FILE_PATH,queue,Constants.CHUNK_SIZE));

        producer.start();

        // Consumers: Process chunks with matchers
        for (int i = 0; i <Constants.THREAD_COUNT; i++) {
            executor.submit(new MatcherWorker(queue, Constants.NAMES, aggregator));
        }

        producer.join();
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        // Aggregator prints results
        aggregator.printResults();
    }
}
