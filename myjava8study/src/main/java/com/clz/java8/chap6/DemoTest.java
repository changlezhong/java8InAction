package com.clz.java8.chap6;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DemoTest {

	public static void main(String[] args) {
		
		// 测试性能
		long t1 = System.currentTimeMillis();
		test(DemoTest::forSum, 1000000);
		System.out.println("forSum: " + " -> " + (System.currentTimeMillis() - t1) + " ms");
		t1 = System.currentTimeMillis();
		test(DemoTest::streamSum, 1000000);
		System.out.println("streamSum: " + " -> " + (System.currentTimeMillis() - t1) + " ms");
		t1 = System.currentTimeMillis();
		test(DemoTest::paralleSum, 1000000);
		System.out.println("paralleSum: " + " -> " + (System.currentTimeMillis() - t1) + " ms");
		t1 = System.currentTimeMillis();
		test(DemoTest::rangedSum, 1000000);
		System.out.println("rangedSum: " + " -> " + (System.currentTimeMillis() - t1) + " ms");
		t1 = System.currentTimeMillis();
		test(DemoTest::paralleRangedSum, 1000000);
		System.out.println("paralleRangedSum: " + " -> " + (System.currentTimeMillis() - t1) + " ms");
		
	}
	
	
	// stream求和
	public static long streamSum(long n) {
		return Stream.iterate(1L, i -> i+1)
					 .limit(n)
					 .reduce(0L, Long::sum);
	}
	// for求和
	public static long forSum(long n) {
		long result = 0;
		for (int i = 0; i <= n; i++) {
			result += i;
		}
		return result;
	}
	//paralle求和
	 public static long paralleSum(long n) {
		 return Stream.iterate(1L, i -> i+1)
				 .limit(n)
				 .parallel()// 将流转换为并行流
				 .reduce(0L, Long::sum);
	 }
	 
	 // 测试性能--运行10次
	 public static void test(Function<Long, Long> function, long n) {
		 long result = 0;
		 for (int i = 0; i < 10; i++) {
			 result = function.apply(n);
		 }
		 System.out.println(result);
	 }
	 
	 // 优化
	 public static long rangedSum(long n) {
		 return LongStream.rangeClosed(0L, n)
				 		  .reduce(0L, Long::sum);
	 }
	 public static long paralleRangedSum(long n) {
		 return LongStream.rangeClosed(0L, n)
				 		  .parallel()
				 		  .reduce(0L, Long::sum);
	 }
	 
}
