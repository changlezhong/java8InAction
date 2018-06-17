package com.clz.java8.chap7;

import java.util.Optional;

public class DemoTest {

	public static void main(String[] args) {
		
		// 声明一个空的Optional
		Optional<Car> car = Optional.empty();
		// 根据一个非空值创建一个Optional
//		Optional<Car> notNullCarOptional = Optional.of(notNullCar);
		// 可接受null的Optional
//		Optional<Car> ofNullableCarOptional = Optional.ofNullable(ofNullableCar);
		
		Person person = null;
		String carInsuranceName = getCarInsuranceName(person);
		System.out.println(carInsuranceName);
		
		
	}
	
	
	static class Person {
        private Optional<Car> car;
        public Optional<Car> getCar() {
            return car;
        }
    }
    
    class Car {
        private Optional<Insurance> insurance;
        public Optional<Insurance> getInsurance() {
            return insurance;
        }
    }
    
    class Insurance {
        private Optional<String> name;
        public Optional<String> getName() {
            return name;
        }
    }
	
	public static String getCarInsuranceName(Person person) {
		// 创建一个可接受null的Optional
		Optional<Person> ofNullable = Optional.ofNullable(person);
		return ofNullable.flatMap(Person::getCar)
						 .flatMap(Car::getInsurance)
						 .flatMap(Insurance::getName)
						 .orElse("Unknown");
	}
}
