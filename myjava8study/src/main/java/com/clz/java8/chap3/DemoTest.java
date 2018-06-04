package com.clz.java8.chap3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
//import static java.util.Comparator.*;
//import static java.util.stream.Collectors.*;

public class DemoTest {

	public static void main(String[] args) {
		
		System.out.println("jdk1.7筛选低卡路里的菜肴名称并按卡路里排序:");
		getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);
		System.out.println("jdk1.8筛选低卡路里的菜肴名称并按卡路里排序：");
		getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);
		
		System.out.println("---------------------------------");
		
		List<String> collect = Dish.menu.stream().filter(d -> d.getCalories() > 300)
						  				.map(Dish::getName)
						  				.limit(3)
						  				.collect(toList());
		System.out.println(collect);
		
		// 流只能遍历一次
		List<String> title = Arrays.asList("java8", "in", "Action");
		Stream<String> stream = title.stream();
		stream.forEach(System.out::println);
//		stream.forEach(System.out::println); // 再次执行将报错
		
		// 中间操作
		collect = Dish.menu.stream().filter(
										d -> {
											System.out.println("filtering:" + d.getName());
										  	return d.getCalories() > 300;
										})
					  				.map(d -> {
					  						System.out.println("maping:" + d.getName());
					  						return d.getName();
					  					})
					  				.limit(3)
					  				.collect(toList());
		// 终端操作
		Dish.menu.stream().forEach(System.out::println);
		
		
	}
	
	
	// jdk1.7筛选低卡路里的菜肴名称并按卡路里排序
	public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes){
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for(Dish d: dishes){
            if(d.getCalories() < 400){
                lowCaloricDishes.add(d);
            }
        }
        List<String> lowCaloricDishesName = new ArrayList<>();
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            public int compare(Dish d1, Dish d2){
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        for(Dish d: lowCaloricDishes){
            lowCaloricDishesName.add(d.getName());
        }
        return lowCaloricDishesName;
    }
	
	// jdk1.8筛选低卡路里的菜肴名称并按卡路里排序
	public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes){
        return dishes.stream()//将集合转化成流
                .filter(d -> d.getCalories() < 400)// 选出低于400卡路里的菜肴
                .sorted(comparing(Dish::getCalories))// 按照卡路里排序
                .map(Dish::getName)// 提取菜肴的名称
                .collect(toList());// 将所有的名称保存在List中
    }
	
}
