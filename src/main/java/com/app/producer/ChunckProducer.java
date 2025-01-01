package com.app.producer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunckProducer implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ChunckProducer.class);

	String filePath;
	BlockingQueue<Map<Integer, String>> queue;
	int chunkSize;
	HashMap<Integer, String> map;

	public ChunckProducer(String filePath, BlockingQueue<Map<Integer, String>> queue, int chunkSize) {
		this.filePath = filePath;
		this.queue = queue;
		this.chunkSize = chunkSize;
	}
	
	/*
	 * Reads the content of the file line by line and adds each line to a Map, 
	 * where the key represents the line number and the value represents the line content.
	 * Once the chunk size reaches 1000, the Map is added to the blocking queue.
	 * At the end of the file, an EOF marker (a special Map with key -1 and value "EOF") 
	 * is inserted into the queue to signal the end of processing.
	 */

	@Override
	public void run() {

		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
			Map<Integer, String> chunk = new HashMap<>();
			String line;
			int lineNumber = 1; // Start line numbers from 1

			while ((line = reader.readLine()) != null) {
				logger.debug("Processing line {}", lineNumber);
				chunk.put(lineNumber, line);
				lineNumber++;
				if (chunk.size() >= chunkSize) {
					logger.debug("Adding chunk of size {} to queue", chunk.size());
					queue.put(new HashMap<>(chunk));
					chunk.clear();
				}
			}

			if (!chunk.isEmpty()) {
				logger.debug("Adding final chunk of size {} to queue", chunk.size());
				queue.put(chunk);
			}
			
			logger.debug("Producer finished processing file.");
			// End-of-file indicator
			queue.put(Collections.singletonMap(-1, "EOF"));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
