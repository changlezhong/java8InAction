package com.clz.java8.chap9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;

public class DemoTest {

	public static void main(String[] args) {
		
		// 测试异步与同步
		long l1 = System.currentTimeMillis();
		Long testAsyn = testAsyn(1000000);
		System.out.println("testAsyn: " + (System.currentTimeMillis() - l1) + " ms, " + testAsyn);
		l1 = System.currentTimeMillis();
		Long testSyn = testSyn(1000000);
		System.out.println("testSyn: " + (System.currentTimeMillis() - l1) + " ms, " + testSyn);
		
	}
	
	
	
	// Future接口--异步
	public static Long testAsyn(long l) {
		
		try {
			// 创建ExecutorService，通过它你可以向线程池提交任务
			ExecutorService executor = Executors.newFixedThreadPool(4);
			List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
			for (int i = 0; i < 500; i++) {
				tasks.add(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						return addNums(l);
					}
				});
			}
			// 异步执行
			List<Future<Long>> invokeAll = executor.invokeAll(tasks);
			// 异步操作的同时执行其他操作
			addNums(l);
			// 获取异步操作的结果，如果被阻塞无法得到结果，那么在最多等待1秒之后退出
			return invokeAll.get(0).get(1, TimeUnit.SECONDS);
		// 当前线程在等待中被中断跑车不异常
		} catch (InterruptedException e) {
			e.printStackTrace();
		// 计算抛出异常
		} catch (ExecutionException e) {
			e.printStackTrace();
		// 在Future对象完成之前超时抛出异常
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
	// 同步
	public static Long testSyn(long l) {
		for (int i = 0; i < 500; i++) {
			addNums(l);
		}
		Long addNums = addNums(l);
		return addNums;
	}
	// 计算从0到给定数值的自然数相加之和
	public static Long addNums(long l) {
		long sum = LongStream.rangeClosed(0L, l)
				  			 .reduce(0L, Long::sum);
		return sum;
	}
	
	
	
	
}


