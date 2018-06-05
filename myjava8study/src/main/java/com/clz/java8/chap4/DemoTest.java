package com.clz.java8.chap4;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.clz.java8.chap3.Dish;;

public class DemoTest {

	public static void main(String[] args) {
		
		// 用谓词筛选
		List<Dish> isVegetarian = Dish.menu.stream().filter(Dish::isVegetarian).collect(toList());
		System.out.println("filter:------------------------------------");
		System.out.println(isVegetarian);
		
		// 筛选各异的元素（去重）
		List<Integer> numbers = Arrays.asList(1,2,1,3,3,2,4);
		numbers.stream().distinct().forEach(System.out::println);
		
		// 截短流
		List<Dish> limit3 = Dish.menu.stream().filter(d -> d.getCalories() > 300).limit(3).collect(toList());
		System.out.println("limit:------------------------------------");
		System.out.println(limit3);

		// 跳过元素
		List<Dish> skip2 = Dish.menu.stream().filter(d -> d.getCalories() > 300).skip(2).collect(toList());
		System.out.println("skip:------------------------------------");
		System.out.println(skip2);
		
		// 筛选前两个荤菜
		List<Dish> meatLimit2 = Dish.menu.stream().filter(d -> d.getType() == Dish.Type.MEAT).limit(2).collect(toList());
		System.out.println("筛选前两个荤菜:------------------------------------");
		System.out.println(meatLimit2);
		
		// map函数
		List<String> mapList = Dish.menu.stream().map(Dish::getName).collect(toList());
		System.out.println("map:------------------------------------");
		System.out.println(mapList);
		
		
		
		
	}
	
}
