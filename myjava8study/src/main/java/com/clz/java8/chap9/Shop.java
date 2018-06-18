package com.clz.java8.chap9;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class Shop {

	public static void main(String[] args) {
		
		// 使用CompletableFuture异步API
		long l1 = System.currentTimeMillis();
		Future<Double> future = new Shop().getPriceSupplyAsync("ab");
		System.out.println("return future : " + (System.currentTimeMillis() - l1) + " ms.");
		// 执行其他更多的任务
		// doSomething
		// 从Future中获取商品价格，如果价格未知，会发生阻塞
		try {
			Double price = future.get();
			System.out.println("return price : " + String.format("%.2f", price) + " --> " + (System.currentTimeMillis() - l1) + " ms.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println("--------------------------------------------------------------------");
		
		List<Shop> shops = Arrays.asList(new Shop("ab"),
				new Shop("qw"),
				new Shop("er"),
				new Shop("as"),
				new Shop("zx"));
		// 使用并行流
		l1 = System.currentTimeMillis();
		List<String> findPrice = findPrice(shops);
		System.out.println("findPrice: " + findPrice + "\n--> " + (System.currentTimeMillis() - l1) + " ms.");
		l1 = System.currentTimeMillis();
		List<String> findPriceParallel = findPriceParallel(shops);
		System.out.println("findPriceParallel: " + findPriceParallel + "\n--> " + (System.currentTimeMillis() - l1) + " ms.");
		l1 = System.currentTimeMillis();
		List<String> findPriceFuture = findPriceFuture(shops);
		System.out.println("findPriceFuture: " + findPriceFuture + "\n--> " + (System.currentTimeMillis() - l1) + " ms.");
		l1 = System.currentTimeMillis();
		List<String> findPriceBestFuture = findPriceBestFuture(shops);
		System.out.println("findPriceBestFuture: " + findPriceBestFuture + "\n--> " + (System.currentTimeMillis() - l1) + " ms.");
		l1 = System.currentTimeMillis();
		List<String> findPriceExecutorFuture = findPriceExecutorFuture(shops);
		System.out.println("findPriceExecutorFuture: " + findPriceExecutorFuture + "\n--> " + (System.currentTimeMillis() - l1) + " ms.");
		
	}
	
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Shop() {
		super();
	}

	public Shop(String name) {
		super();
		this.name = name;
	}

	// 获取商品价格的方法
	public double getPrice(String product) {
		return calculatePrice(product);
	}
	
	// 模拟1秒延迟的方法
	public static void delay() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 计算商品价格的方法
	public double calculatePrice(String product) {
		delay();
		return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
	}
	
	/**
	 * 异步API
	 */
	public Future<Double> getPriceAsyn(String product){
		// 创建 CompletableFuture 对象
		CompletableFuture<Double> future = new CompletableFuture<Double>();
		// 启动一个线程执行异步计算
		new Thread(() -> {
			double price = calculatePrice(product);
			future.complete(price);
		}).start();
		
		return future;
	}
	
	/**
	 * 异常处理
	 */
	public Future<Double> getPriceAsynException(String product){
		// 创建 CompletableFuture 对象
		CompletableFuture<Double> future = new CompletableFuture<Double>();
		// 启动一个线程执行异步计算
		new Thread(() -> {
			try {
				double price = calculatePrice(product);
				// 如果价格计算正确结束，完成Future操作并设置商品价格
				future.complete(price);
			} catch (Exception e) {
				// 否则就抛出导致失败的异常，完成这次Future操作
				future.completeExceptionally(e);
			}
		}).start();
		
		return future;
	}
	
	/**
	 * 使用工厂方法supplyAsync创建CompletableFuture
	 */
	public Future<Double> getPriceSupplyAsync(String product){
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}
	
	/**
	 * 使用并行流对请求进行并行操作
	 */
	// 顺序流
	public static List<String> findPrice(List<Shop> shops) {
		return shops.stream()
					.map(shop -> {
						try {
							return String.format("%s 价格： %.2f", shop.getName(), shop.getPriceSupplyAsync(shop.getName()).get());
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						return null;
					})
					.collect(Collectors.toList());
	}
	// 并行流
	public static List<String> findPriceParallel(List<Shop> shops) {
		return shops.parallelStream()
				.map(shop -> {
					try {
						return String.format("%s 价格： %.2f", shop.getName(), shop.getPriceSupplyAsync(shop.getName()).get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				})
				.collect(Collectors.toList());
	}
	
	/**
	 * 使用CompletableFuture发起异步请求
	 */
	public static List<String> findPriceFuture(List<Shop> shops) {
		return shops.stream()
				 .map(shop -> CompletableFuture.supplyAsync(() -> {
					try {
						return String.format("%s 价格： %.2f", shop.getName(), shop.getPriceSupplyAsync(shop.getName()).get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				})).map(CompletableFuture::join).collect(Collectors.toList());
	}
	// 优化
	public static List<String> findPriceBestFuture(List<Shop> shops) {
		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> {
					try {
						return String.format("%s 价格： %.2f", shop.getName(), shop.getPriceSupplyAsync(shop.getName()).get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				})).collect(Collectors.toList());
		return priceFutures.stream()
						   .map(CompletableFuture::join)
						   .collect(Collectors.toList());
	}
	
	/**
	 * 使用定制的执行器
	 */
	public static List<String> findPriceExecutorFuture(List<Shop> shops) {
		final ExecutorService executor = Executors.newFixedThreadPool(5);
		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> {
					try {
						return String.format("%s 价格： %.2f", shop.getName(), shop.getPriceSupplyAsync(shop.getName()).get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				}, executor)).collect(Collectors.toList());
		return priceFutures.stream()
						   .map(CompletableFuture::join)
						   .collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	
	
	
	
}


