package com.clz.java8.chap2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.clz.java8.chap1.Apple;

public class UserLambda {

	// 测试数据
	public static List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));
	
	public static void main(String[] args) {
		
		// 使用Predicate
		System.out.println("使用Predicate:");
		List<String> list = new ArrayList<String>();
		list.add(null);
		list.add("Hello World");
		list.add("");
		Predicate<String> p = (String s) -> s != null && !s.isEmpty();
		System.out.println(filter(list , p));
		
		// 使用Consumer
		System.out.println("// 使用Consumer:");
		forEach(Arrays.asList(1,2,3,4,5), (Integer i) -> System.out.println(i));
		
		// 使用Function
		System.out.println("// 使用Function:");
		List<Integer> l = map(Arrays.asList("lambda", "in", "action"), (String s) -> s.length());
		System.out.println(l);
		
		// 省去参数类型
		List<Apple> heavirThan150 = filter(inventory, a -> a.getWeight() > 150);
		System.out.println(heavirThan150);
		Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
		c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
		
		// 方法的引用
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
		inventory.sort(Comparator.comparing((Apple a) -> a.getWeight()));
		inventory.sort(Comparator.comparing(Apple::getWeight));
		
		BiFunction<String, Integer, String> bf = (String str, Integer i) -> str.substring(i); 
		bf = String::substring;
		String methadQuote = methadQuote("Hello", 2, bf);
		System.out.println("方法的引用:");
		System.out.println(methadQuote);
		Consumer<String> consumer = (String str) -> System.out.println(str);
		consumer = System.out::println;
		methadQuote1("Hello World", consumer);
		
		/**
		 *  构造函数引用
		 */
		// 无参
		Supplier<Apple> s = Apple::new;
		// 一个参数
		Function<String, Apple> f = Apple::new;
		// 两个参数
		BiFunction<Integer, String, Apple> biFunction = Apple::new;
		
		// 自定义的函数式接口
		TriFunction<String, Integer, String, Student> tri = Student::new;
		
		/**
		 * 复合Lambda表达式的有用方法
		 */
		// 1. 比较器复合
		Comparator<Apple> com = Comparator.comparing(Apple::getWeight);
		// 逆序
		inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
		// 比较器链
		inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
		
		// 2. 谓词复合
		Predicate<Apple> redApple = (Apple a) -> "red".equals(a.getColor());
		Predicate<Apple> notRedApple = redApple.negate();// 产生现有对象redApple的非
		Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);
		Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
														   .or(a -> "green".equals(a.getColor()));
		List<Apple> notRedApples = filter(inventory, notRedApple);
		List<Apple> redAndHeavyApples = filter(inventory, redAndHeavyApple);
		List<Apple> redAndHeavyAppleOrGreenApples = filter(inventory, redAndHeavyAppleOrGreen);
		System.out.println("notRedApples:");
		System.out.println(notRedApples);
		System.out.println("redAndHeavyApples");
		System.out.println(redAndHeavyApples);
		System.out.println("redAndHeavyAppleOrGreenApples");
		System.out.println(redAndHeavyAppleOrGreenApples);
		
		// 3. 函数复合
		Function<Integer, Integer> g = x -> x + 1;
		Function<Integer, Integer> h = y -> y * 2;
		Function<Integer, Integer> j = g.andThen(h);// 相当于数学中的 h(g(x))
		Integer apply = j.apply(1);
		System.out.println("andThen函数复合：" + apply);// 4
		j = g.compose(h);// 相当于数学中的 g(h(x))
		apply = j.apply(1);
		System.out.println("compose函数复合：" + apply);// 3
				
	}
	
	// 使用Predcate
	public static <T> List<T> filter(List<T> list, Predicate<T> p){
		List<T> result = new ArrayList<T>();
		for (T t : list) {
			if(p.test(t)) {
				result.add(t);
			}
		}
		return result;
	}
	
	// 使用Consumer
	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for(T t : list) {
			c.accept(t);
		}
	}
	
	// 使用Function
	public static <T, R> List<R> map(List<T> list, Function<T, R> f){
		List<R> result = new ArrayList<R>();
		for (T t : list) {
			result.add(f.apply(t));
		}
		return result;
	}
	
	// 方法的引用
	public static String methadQuote(String str, Integer i, BiFunction<String, Integer, String> bf) {
		return bf.apply(str, i);
	}
	public static void methadQuote1(String str, Consumer<String> c) {
		c.accept(str);
	}
	 
}
