package com.clz.java8.chap1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DemoTest {

	// 测试数据
	public static List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));
	
	public static void main(String[] args) {
		
		// 测试筛选绿色的苹果
		List<Apple> filterGreenApples = filterGreenApples(inventory);
		System.out.println("测试筛选绿色的苹果：");
		System.out.println(filterGreenApples);
		
		// 测试筛选绿色的苹果
		List<Apple> filterRedApples = filterRedApples(inventory);
		System.out.println("测试筛选绿色的苹果：");
		System.out.println(filterRedApples);
		
		// 根据传入的颜色来筛选苹果
		List<Apple> filterApplesByColor = filterApplesByColor(inventory, "red");
		System.out.println("根据传入的颜色来筛选苹果：");
		System.out.println(filterApplesByColor);
		
		// 根据传入的重量来筛选苹果
		List<Apple> filterApplesByWeight = filterApplesByWeight(inventory, 150);
		System.out.println("根据传入的重量来筛选苹果：");
		System.out.println(filterApplesByWeight);
		
		// 根据红色并且重的苹果抽象条件进行筛选
		List<Apple> filterApples = filterApples(inventory, new AppleRedAndHeavyPredicate());
		System.out.println("根据红色并且重的苹果抽象条件进行筛选：");
		System.out.println(filterApples);
		
		// 使用匿名类筛选绿色并且重的苹果
		List<Apple> filterApples2 = filterApples(inventory, new ApplePredicate() {
			@Override
			public boolean test(Apple a) {
				return "green".equals(a.getColor()) && a.getWeight() > 150;
		}});
		System.out.println("使用匿名类筛选绿色并且重的苹果：");
		System.out.println(filterApples2);
		
		// 使用Lambda表达式筛选绿色苹果
		List<Apple> filterApples3 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
		System.out.println("使用Lambda表达式筛选绿色苹果:");
		System.out.println(filterApples3);
		
		// 使用Lambda表达式筛选绿色苹果并且重的苹果
		List<Apple> filterApples4 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()) && a.getWeight() > 150);
		System.out.println("使用Lambda表达式筛选绿色并且重的苹果:");
		System.out.println(filterApples4);
		
		// 将List类型抽象化
		List<Apple> filter = filter(inventory, (Apple a) -> "green".equals(a.getColor()));
		System.out.println("将List类型抽象化:");
		System.out.println(filter);
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		List<Integer> filter2 = filter(list , (Integer i) -> i % 2 == 0);
		System.out.println(filter2);
		
		// 真实的例子
//		inventory.sort(new Comparator<Apple>() {
//			@Override
//			public int compare(Apple a1, Apple a2) {
//				return a1.getWeight().compareTo(a2.getWeight());
//		}});
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
		System.out.println("真实的例子:");
		System.out.println(inventory);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello World");
			}});
		t.start();
		Thread t1 = new Thread(() -> System.out.println("Hello World"));
		t1.start();
	}
	
/**
 ***************************************************************************************************************** 	
 */
	
	
	// 筛选绿色的苹果
	public static List<Apple> filterGreenApples(List<Apple> list){
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : list) {
			if("green".equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// 筛选红色的苹果
	public static List<Apple> filterRedApples(List<Apple> list){
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : list) {
			if("red".equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// 根据传入的颜色来筛选苹果
	public static List<Apple> filterApplesByColor(List<Apple> list, String color){
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : list) {
			if(color.equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// 根据传入的重量来筛选苹果
	public static List<Apple> filterApplesByWeight(List<Apple> list, int weight){
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : list) {
			if(apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// 根据红色并且重的苹果抽象条件进行筛选
	public static List<Apple> filterApples(List<Apple> list, ApplePredicate ap){
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : list) {
			if(ap.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// 将List类型抽象化
	public static <T> List<T> filter(List<T> list, Predicate<T> p){
		List<T> result = new ArrayList<T>();
		for (T t : list) {
			if(p.test(t)) {
				result.add(t);
			}
		}
		return result;
	}
	
}
