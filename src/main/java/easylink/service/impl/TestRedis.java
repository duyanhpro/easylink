package easylink.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class TestRedis {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();

			int n = 1000000;
			start = System.currentTimeMillis();
			
			// Connect to redis
		    Jedis jedis;
		    if (args.length == 0) jedis = new Jedis("10.84.70.146", 6378);
		    else 
		    	jedis = new Jedis (args[0], 6379);
		    
		    start = System.currentTimeMillis();
		    // Using normal String key-value
		    // 10k record: 37s, 31s, 20s (night), 35s
//		    for (int i=0; i<n;i++) {
//			    jedis.set("mnp-anhpd-" + String.format("%010d", i), "1");				    	
//		    }
//		    System.out.println("Key-value:  " + (System.currentTimeMillis()-start) + " ms");
//		    start = System.currentTimeMillis();
		    
		    // Redis Sets: Un-ordered list; exclude duplicate members (how to empty?)
		    //	https://codedestine.com/redis-jedis-set-java/
		    // Using redis Sets: 1000 --> 2s, 5.8s, 3.6s, 1.7s, 1.7s, 12s, 1.8s
		    // 10k item: 28s, 19s, 21s, 39s (2 process //)
//		    for (int i=0; i<n;i++) {
//			    jedis.sadd("mnp-anhpd", String.format("%010d", i));				    	
//		    }
//		    System.out.println("Set: " + (System.currentTimeMillis()-start) + " ms");
		    
		    start = System.currentTimeMillis();
		    // Using redis hashes: 10k --> 33s
//		    for (int i=0; i<n;i++) {
//			    jedis.hset("mnp-anhpd-hash", String.format("%010d", i), "1");				    	
//		    }
//		    System.out.println("Hset: " + (System.currentTimeMillis()-start) + " ms");

//		    Pipeline p = jedis.pipelined();
		    // Test result:  1mil: 57s, 54s
//		    // hset insert using redis pipeline
//		    for (int i=0; i<n;i++) {
//		    	p.hset("mnp-anhpd-hash", String.format("%010d", i), "1");
//		    	if (i % 100 == 0) {
//		    		p.sync();
//		    		p.close();
//		    		p = jedis.pipelined();
//		    	}
//		    }
//		    System.out.println("Pipeline 100 hset: " + (System.currentTimeMillis()-start) + " ms");
		    
		    // local map
		    // 1mil record: 2.6s
		    Map<String, String> myMap = new HashMap<String, String>();
		    for (int i=0; i<n;i++) {
		    	myMap.put(String.format("%010d", i), "1");
		    }
		    System.out.println("Local map: " + (System.currentTimeMillis()-start) + " ms");
		    
		    
			// Test random select from redis
//			start = System.currentTimeMillis();
//			for (int j=0; j<100000; j++) {
//				int i = RandomUtil.getRadomInteger(0, 999999);
//				//int op = mnpMap.get(String.format("%010d", i));		// will exception if null
//				//System.out.println(op);
//			}
			
			jedis.close();
		System.out.println("Finish in " + (System.currentTimeMillis()-start) + " ms");

	}

}
