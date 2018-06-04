package com.clz.java8.chap1;

public class AppleRedAndHeavyPredicate implements ApplePredicate{

	@Override
	public boolean test(Apple a) {
		
		return "red".equals(a.getColor()) && a.getWeight() > 150;
	}

}
