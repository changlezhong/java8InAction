package com.clz.java8.chap4;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.clz.java8.chap3.Dish;

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
		
		// flatMap函数
		List<String> list = Arrays.asList("Hello", "World");
		List<String[]> wordArr = list.stream().map(word -> word.split("")).distinct().collect(toList());
		System.out.println(wordArr);
		
		// 1. Arrays.stream()
		String[] array = {"Hello", "World"};
		Stream<String> stream = Arrays.stream(array);
		List<Stream<String>> strngStream = list.stream().map(word -> word.split("")).map(Arrays::stream).distinct().collect(toList());
		// 2. 使用flatMap
		List<String> words = list.stream().map(word -> word.split("")).flatMap(Arrays::stream).distinct().collect(toList());
		System.out.println(words);
		
		// anyMatch
		System.out.println("anyMatch:");
		System.out.println(Dish.menu.stream().anyMatch(Dish::isVegetarian));;
		
		// allMatch
		System.out.println("allMatch:");
		System.out.println(Dish.menu.stream().allMatch(d -> d.getCalories() < 1000));
		
		// noneMatch
		System.out.println("noneMatch");
		System.out.println(Dish.menu.stream().noneMatch(d -> d.getCalories() >= 1000));
		
		// findAny
		System.out.println("findAny:");
		// Optional类是一个容器类，代表一个值存在或者不存在，用来解决空指针异常。Optional类后面细讲
		Optional<Dish> findAny = Dish.menu.stream().filter(Dish::isVegetarian).findAny();
		if(findAny.isPresent()) {
			System.out.println(findAny.get().getName());
		}
		Dish.menu.stream().filter(Dish::isVegetarian).findAny().ifPresent(d -> System.out.println(d.getName()));
		
		// findFirst
		List<Integer> someNumbers = Arrays.asList(1,2,3,4,5);
		Optional<Integer> findFirst = someNumbers.stream().map(x -> x * x).filter(x -> x % 3 == 0).findFirst();
		if(findFirst.isPresent()) {
			System.out.println(findFirst.get());
		}
		
		// reduce
		Integer sum = someNumbers.stream().reduce(0, (a, b) -> a + b);
		System.out.println(sum);
		sum = someNumbers.stream().reduce(0, Integer::sum);
		System.out.println(sum);
		Optional<Integer> reduce = someNumbers.stream().reduce(Integer::sum);
		if(reduce.isPresent()) {
			System.out.println(reduce.get());
		}
		
		// 最大值和最小值
		Optional<Integer> max = someNumbers.stream().reduce(Integer::max);
		if(max.isPresent()) {
			System.out.println(max.get());
		}
		someNumbers.stream().reduce(Integer::min).ifPresent(min -> System.out.println(min));;
		
		
		// 测试数据
		Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
		
		List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300), 
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),	
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );
		
		System.out.println("-------------------------------------------------------------------------");
		
//		1. 找出2011年发生的所有交易，并按交易额排序（从低到高）
		List<Transaction> sort1 = transactions.stream().filter(t -> t.getYear() == 2011)
							 .sorted(comparing(Transaction::getValue))
							 .collect(toList());
		System.out.println("1. 找出2011年发生的所有交易，并按交易额排序（从低到高）:");
		System.out.println(sort1);
		// 倒序1
		List<Transaction> sort2 = transactions.stream().filter(t -> t.getYear() == 2011)
				 .sorted((t1, t2) -> ((Integer) t2.getValue()).compareTo(t1.getValue()))
				 .collect(toList());
		System.out.println("1. 找出2011年发生的所有交易，并按交易额排序（从高到低）:");
		System.out.println(sort2);
		// 倒序2
		List<Transaction> sort3 = transactions.stream().filter(t -> t.getYear() == 2011).collect(toList());
		Comparator<Transaction> c = (t1, t2) -> ((Integer) t1.getValue()).compareTo(t2.getValue());
		sort3.sort(c.reversed());
		System.out.println("1. 找出2011年发生的所有交易，并按交易额排序（从高到低）:");
		System.out.println(sort3);
		
//		2. 交易员都在哪些不同的城市工作过
		List<String> citys = transactions.stream().map(t -> t.getTrader().getCity()).distinct().collect(toList());
		System.out.println("2. 交易员都在哪些不同的城市工作过:");
		System.out.println(citys);
//		3. 查找所有来自剑桥的交易员，并按姓名排序
		List<Trader> names = transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge"))
							 .map(Transaction::getTrader)
							 .distinct()
							 .sorted(comparing(Trader::getName))
							 .collect(toList());
		System.out.println("3. 查找所有来自剑桥的交易员，并按姓名排序:");
		System.out.println(names);
//	　	4. 返回所有交易员的姓名字符串，按字母顺序排序
		String nameStr = transactions.stream().map(t -> t.getTrader().getName())
							 .distinct()
							 .sorted()
							 .reduce("", (n1, n2) -> n1 + n2);
		System.out.println("4. 返回所有交易员的姓名字符串，按字母顺序排序:");
		System.out.println(nameStr);
		// 优化（reduce对字符串做相加的时候效率不高，每次迭代都会创建一个新的String对象，可以用joining(其内部用到StringBuild)），下一章会讲到
		nameStr = transactions.stream().map(t -> t.getTrader().getName())
							 .distinct()
							 .sorted()
							 .collect(Collectors.joining());
		System.out.println(nameStr);
//		5. 有没有交易员是在米兰工作的
		boolean anyMatch = transactions.stream().anyMatch(t -> t.getTrader().getCity().equals("Milan"));
		System.out.println("5. 有没有交易员是在米兰工作的:");
		System.out.println(anyMatch);
//		6. 打印生活在剑桥的交易员的所有交易额
		System.out.println("6. 打印生活在剑桥的交易员的所有交易额:");
		transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge"))
							 .map(t -> t.getValue())
							 .forEach(System.out::println);
//		7. 所有交易中，最高的交易额是多少
		Optional<Integer> maxT = transactions.stream().map(Transaction::getValue).reduce(Integer::max);
		System.out.println("7. 所有交易中，最高的交易额是多少:");
		System.out.println(maxT.get());
//		8. 找出交易额最小的交易
		Optional<Transaction> minT = transactions.stream().reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
		System.out.println("8. 找出交易额最小的交易:");
		System.out.println(minT.get());
		// 流也支持min和max方法
		minT = transactions.stream().min(comparing(Transaction::getValue));
		System.out.println(minT.get());
		
		// 计算菜单热量的综合
		Integer sumCalories = Dish.menu.stream().map(Dish::getCalories).reduce(0, Integer::sum);
		System.out.println(sumCalories);
		
		// 1. 映射到数值流
		int intStreamSum = Dish.menu.stream().mapToInt(Dish::getCalories).sum();
		System.out.println(intStreamSum);
		// 2. 转换成一般流
		IntStream intStream = Dish.menu.stream().mapToInt(Dish::getCalories);
		Stream<Integer> integerStream = intStream.boxed();
		// 3. OptionalInt
		OptionalInt maxCalories = Dish.menu.stream().mapToInt(Dish::getCalories).max();
		int defaultMaxCalories = maxCalories.orElse(1); // 如果没有最大值的话，显式提供一个默认的最大值
		System.out.println(defaultMaxCalories);
		
	}
	
}
