package com.clz.java8.chap2;

public class LambdaDemo {
	
	public static void main(String[] args) {
		
		Runnable r1 = () -> System.out.println("Hello World1");
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello World2");
			}
		};

		process(r1);
		process(r2);
		process(() -> System.out.println("Hello World3"));
		
	}
	
	public static void process(Runnable r) {
		r.run();
	}
	
}
