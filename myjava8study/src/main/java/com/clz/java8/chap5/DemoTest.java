package com.clz.java8.chap5;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.clz.java8.chap3.Dish;
import com.clz.java8.chap3.Dish.Type;

public class DemoTest {
	
	public static void main(String[] args) {
		
		// maxBy和minBy
		Optional<Dish> maxClories = Dish.menu.stream().collect(maxBy(Comparator.comparingInt(Dish::getCalories)));
		if(maxClories.isPresent()){
			System.out.println("maxBy : " + maxClories.get().getCalories());
		}
		Optional<Dish> minBy = Dish.menu.stream().collect(minBy(Comparator.comparingInt(Dish::getCalories)));
		if(minBy.isPresent()) {
			System.out.println("minBy : " + minBy.get().getCalories());
		}
		
		// summingInt(summingLong, summingDouble)
		Integer sumClories = Dish.menu.stream().collect(summingInt(Dish::getCalories));
		System.out.println("summingInt : " + sumClories);
		// averagingInt(averagingLong, averagingDouble)
		Double avgClories = Dish.menu.stream().collect(averagingInt(Dish::getCalories));
		System.out.println("averagingInt : " + avgClories);
		// summarizingInt
		IntSummaryStatistics intSummaryStatistics = Dish.menu.stream().collect(summarizingInt(Dish::getCalories));
		System.out.println("count : " + intSummaryStatistics.getCount());
		System.out.println("sum : " + intSummaryStatistics.getSum());
		System.out.println("max : " + intSummaryStatistics.getMax());
		System.out.println("min : " + intSummaryStatistics.getMin());
		System.out.println("avg : " + intSummaryStatistics.getAverage());
		
		// joining
		String names = Dish.menu.stream().map(Dish::getName).collect(joining());
		System.out.println(names);
		names = Dish.menu.stream().map(Dish::getName).collect(joining(", "));
		System.out.println(names);
	
		// groupingBy
		Map<Type, List<Dish>> mapByType = Dish.menu.stream().collect(groupingBy(Dish::getType));
		System.out.println(mapByType);
		Map<String, List<Dish>> mapByCal = Dish.menu.stream().collect(groupingBy(d -> {
			if(d.getCalories() <= 400) {
				return "低热量";
			} else if(d.getCalories() <= 700) {
				return "中等热量";
			} else {
				return "高热量";
			}
		}));
		System.out.println(mapByCal);
		
		// 二级分组
		Map<Type, Map<String, List<Dish>>> mapMap = Dish.menu.stream().collect(groupingBy(Dish::getType, groupingBy(d -> {
			if(d.getCalories() <= 400) {
				return "低热量";
			} else if(d.getCalories() <= 700) {
				return "中等热量";
			} else {
				return "高热量";
			}
		})));
		System.out.println(mapMap);
	
		//统计每类菜的个数
		Map<Type, Long> typeCount = Dish.menu.stream().collect(groupingBy(Dish::getType, counting()));
		System.out.println(typeCount);
	
		// 找出每类菜肴中热量最高的菜肴
		Map<Type, Optional<Dish>> typeMaxCalMap = Dish.menu.stream().collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
		System.out.println(typeMaxCalMap);
		// 将收集器的结果转换成另一种类型
		Map<Type, Dish> _typeMaxCalMap = Dish.menu.stream().collect(
														groupingBy(Dish::getType, 
																collectingAndThen(
																		maxBy(comparing(Dish::getCalories)), 
																			  Optional::get)));
		System.out.println(_typeMaxCalMap);
	
		// 对每一类菜肴的热量求和
		Map<Type, Integer> sumCalMap = Dish.menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
		System.out.println(sumCalMap);
		
		// mapping
		Map<Type, Set<String>> mappingMap = Dish.menu.stream().collect(groupingBy(Dish::getType, mapping(d -> {
			if(d.getCalories() <= 400) {
				return "低热量";
			} else if(d.getCalories() <= 700) {
				return "中等热量";
			} else {
				return "高热量";
			}
		}, toSet())));
		System.out.println(mappingMap);
		// toCollection
		Map<Type, HashSet<String>> toCollectionMap = Dish.menu.stream().collect(groupingBy(Dish::getType, mapping(d -> {
			if(d.getCalories() <= 400) {
				return "低热量";
			} else if(d.getCalories() <= 700) {
				return "中等热量";
			} else {
				return "高热量";
			}
		}, toCollection(HashSet::new))));
		System.out.println(toCollectionMap);
		
		// 分区
		Map<Boolean, List<Dish>> isVegeMap = Dish.menu.stream().collect(partitioningBy(Dish::isVegetarian));
		System.out.println(isVegeMap);
		// 是否为素食分区，按菜肴类型分组
		Map<Boolean, Map<Type, List<Dish>>> isVegeTypeMap = Dish.menu.stream().
							collect(
									partitioningBy(Dish::isVegetarian, 
											groupingBy(Dish::getType)));
		System.out.println(isVegeTypeMap);
		// 找出素食与非素食中热量最高的菜肴
		Map<Boolean, Dish> vegeMaxCalMap = Dish.menu.stream().collect(
				partitioningBy(Dish::isVegetarian, 
						collectingAndThen(
								maxBy(
										comparingInt(Dish::getCalories)), Optional::get)));
		System.out.println(vegeMaxCalMap);
		
		// 将数字按质数和非质数分区
		Map<Boolean, List<Integer>> partitionPrimes = partitionPrimes(15);
		System.out.println(partitionPrimes);
		
		// 总结
		Stream<Dish> menuStream = Dish.menu.stream();
		// toList
		
		
		
		
	}
	
	// 判断一个数是否为质数(质数定义为在大于1的自然数中，除了1和它本身以外不再有其他因数！)
	private static boolean isPrime(int n) {
		return IntStream.range(2, n).noneMatch(i -> n % i == 0);
	}
	// 优化
	private static boolean isNotPrime(int n) {
		// 1. 仅测试小于等于测试数字的平方根的因子即可
		int sqrt = (int) Math.sqrt(n);
		// 2. noneMatch将会对所有元素进行匹配，可以使用anyMatch
		return !IntStream.rangeClosed(2, sqrt).anyMatch(i -> n % i == 0);
	}
	// 质数与非质数分区的方法
	public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(i -> isNotPrime(i)));
	}
	
	
	
}
