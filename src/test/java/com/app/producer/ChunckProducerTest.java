package com.app.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.app.producer.ChunckProducer;

@Test
public class ChunckProducerTest {
	
	String filePath="src/test/resources/test";
	BlockingQueue<Map<Integer, String>> queue=new LinkedBlockingQueue<>();
	int chunkSize=2;
	ChunckProducer producer; 
	
	@BeforeClass
	public void init() {
		producer=new ChunckProducer(filePath, queue, chunkSize);
	}

	 @Test
	 public void testEnqueue() throws InterruptedException {
		 
		 Thread prodThread=new Thread(producer);
		 prodThread.start();
		 prodThread.join();
		 Assert.assertEquals(queue.size(), 4);
	 }
	 
	 @Test
	 public void testQueueData() throws InterruptedException {
			HashMap<Integer, String> map1 = new HashMap<Integer, String>();
			map1.put(1, "James and John went to the market.");
			map1.put(2, "John is playing football.");
			HashMap<Integer, String> map2 = new HashMap<Integer, String>();
			map2.put(3, "Robert is studying in the library.");
			map2.put(4, "Michael and William are good friends.");
			HashMap<Integer, String> map3 = new HashMap<Integer, String>();
			map3.put(5, "David loves hiking and cycling.");
			map3.put(6, "Richard is learning Java programming.");

			Thread prodThread = new Thread(producer);
			prodThread.start();
			prodThread.join();

			Map<Integer, String> chunk1 = queue.take();
			Map<Integer, String> chunk2 = queue.take();
			Map<Integer, String> chunk3 = queue.take();
			
			Assert.assertEquals(map1, chunk1);
			Assert.assertEquals(map2, chunk2);
			Assert.assertEquals(map3, chunk3);
	 }
}
