package com.app.consumer;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.aggregator.Aggregator;
import com.app.model.Result;

public class MatcherWorker implements Runnable {
	private final BlockingQueue<Map<Integer, String>> queue;
    private final List<String> names;
    private final Aggregator aggregator;
    private static final Logger logger = LoggerFactory.getLogger(MatcherWorker.class);

    public MatcherWorker(BlockingQueue<Map<Integer, String>> queue, List<String> names, Aggregator aggregator) {
        this.queue = queue;
        this.names = names;
        this.aggregator = aggregator;
    }
    
    /*
     * dequeues chunks from the queue, searches for target names within each chunk, 
     * and sends the matching results to the aggregator for consolidation.
     */

    @Override
    public void run() {
        try {
            while (true) {
            	logger.debug("Dequeing chuncks from queue");
                Map<Integer, String> chunk = queue.take();
                if (chunk.containsKey(-1)) {
                    queue.put(chunk); // Pass EOF to other workers
                    break;
                }

                Map<String, List<Result>> matches = new HashMap<>();
                for (Map.Entry<Integer, String> entry : chunk.entrySet()) {
                    int lineNumber = entry.getKey();
                    String line = entry.getValue();

                    for (String name : names) {
                        int charOffset = 0;
                        while ((charOffset = line.indexOf(name, charOffset)) != -1) {
                            matches
                                .computeIfAbsent(name, k -> new ArrayList<>())
                                .add(new Result(lineNumber, charOffset));
                            charOffset += name.length();
                        }
                    }
                }

                aggregator.aggregate(matches);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}