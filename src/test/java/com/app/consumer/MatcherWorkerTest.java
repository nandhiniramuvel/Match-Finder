package com.app.consumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.app.aggregator.Aggregator;
import com.app.model.Result;

import static org.mockito.Mockito.*;

@Test
public class MatcherWorkerTest {
	private BlockingQueue<Map<Integer, String>> queue;
    private Aggregator aggregator;
    private List<String> names;
    private MatcherWorker matcherWorker;
    
    @BeforeClass
    public void setUp() throws InterruptedException {
        queue=new LinkedBlockingQueue<Map<Integer,String>>();
        queue.put(new HashMap(){{ put(1, "James and John went to the market."); }});
        queue.put(new HashMap(){{ put(2, "Robert went to the store."); }});
        queue.put(new HashMap(){{ put(-1, "EOF"); }});
        aggregator = new Aggregator();
        names = Arrays.asList("James", "John", "Robert");
        matcherWorker = new MatcherWorker(queue, names, aggregator);
    }

    @Test
    public void testRunWithMatchingData() throws InterruptedException {

        Thread workerThread = new Thread(matcherWorker);
        workerThread.start();
        workerThread.join();  

        Map<String, List<Result>> expectedMatches = new HashMap();
        expectedMatches.put("James", Arrays.asList(new Result(1, 0)));
        expectedMatches.put("John", Arrays.asList(new Result(1, 10)));
        expectedMatches.put("Robert", Arrays.asList(new Result(2, 0)));

        Map<String, List<Result>> actualMatches = aggregator.getMatches();
        
        Assert.assertEquals(actualMatches, expectedMatches);
    }
}
