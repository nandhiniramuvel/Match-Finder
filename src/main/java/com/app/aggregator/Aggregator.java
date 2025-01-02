package com.app.aggregator;

import java.util.*;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.model.Result;

public class Aggregator {
	private final Map<String, List<Result>> aggregatedResults = new ConcurrentHashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);
	
	//Aggregates the matching results from worker threads into a consolidated map.
	public synchronized void aggregate(Map<String, List<Result>> results) {
		logger.debug("Consolidating Matches");
		for (Map.Entry<String, List<Result>> entry : results.entrySet()) {
			aggregatedResults.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
		}
	}
     
	//prints the consolidated map
	public void printResults() {
		logger.info("Final aggregated results:");
		aggregatedResults.forEach((name, locations) -> {
			System.out.println(name + " --> " + locations);
		});
	}

	public Map<String, List<Result>> getMatches() {
		return aggregatedResults;
	}
}