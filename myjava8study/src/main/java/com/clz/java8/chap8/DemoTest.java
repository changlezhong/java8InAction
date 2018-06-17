package com.clz.java8.chap8;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.*;

public class DemoTest {

	public static void main(String[] args) {
		
		// 创建一个LocalDate对象并读取它的值
		LocalDate localDate = LocalDate.of(2018, 06, 17);
		int year = localDate.getYear();
		Month month = localDate.getMonth();
		int dayOfMonth = localDate.getDayOfMonth();
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		int lengthOfMonth = localDate.lengthOfMonth();
		boolean leapYear = localDate.isLeapYear();
		System.out.println("year:" + year + ", month:" + month + ", dayOfMonth:" + 
						   dayOfMonth + ", dayOfWeek:" + dayOfWeek + ", lengthOfMonth:" + 
						   lengthOfMonth + ", leapYear:" + leapYear);
		
		// 获取当前时间的日期
		LocalDate now = LocalDate.now();
		System.out.println(now);
		
		// 使用TemporalField读取LocalDate的值
		year = localDate.get(ChronoField.YEAR);
		int monthVal = localDate.get(ChronoField.MONTH_OF_YEAR);
		int day = localDate.get(ChronoField.DAY_OF_MONTH);
		System.out.println(year + "-" + String.format("%02d", monthVal) + "-" + day);
		
			
		// 创建LocalTime并读取它的值
		LocalTime localTime = LocalTime.of(10, 24, 30);
		int hour = localTime.getHour();
		int minute = localTime.getMinute();
		int second = localTime.getSecond();
		System.out.println(hour + ":" + minute + ":" + second);
		
		// 使用parse
		LocalDate parseDate = LocalDate.parse("2018-06-17");
		LocalTime parseTime = LocalTime.parse("10:24:30");
		System.out.println(parseDate);
		System.out.println(parseTime);
		
		// LocalDateTime
		LocalDateTime dt1 = LocalDateTime.of(2018, 6, 17, 10, 24, 30);
		LocalDateTime dt2 = LocalDateTime.of(parseDate, parseTime);
		LocalDateTime dt3 = parseDate.atTime(parseTime);
		LocalDateTime dt4 = parseDate.atTime(10, 24, 30);
		LocalDateTime dt5 = parseTime.atDate(parseDate);
		System.out.println(dt1);
		System.out.println(dt2);
		System.out.println(dt3);
		System.out.println(dt4);
		System.out.println(dt5);
		
		// 从LocalDateTime中提取LocalDate和LocalTime
		LocalDateTime ldt = LocalDateTime.of(2018, 6, 17, 10, 24, 30);
		LocalDate ld = ldt.toLocalDate();
		LocalTime lt = ldt.toLocalTime();
		System.out.println(ld);
		System.out.println(lt);
		
		// 时间比较
		int compareTo = dt1.compareTo(dt5);
		System.out.println(compareTo);
		
		// Duration
		Duration duration = Duration.between(LocalDateTime.of(2018, 5, 17, 10, 23, 30), 
											 LocalDateTime.of(2018, 6, 17, 10, 24, 29));
		System.out.println(duration.getSeconds());
		System.out.println(duration.toMillis());
		// Period
		Period period = Period.between(LocalDate.of(2018, 5, 16), 
									   LocalDate.of(2018, 6, 16));
		System.out.println(period.getDays());
		System.out.println(period.getMonths());
		
		// 操纵、解析和格式化时间
		LocalDate ld1 = LocalDate.of(2018, 6, 17);
		LocalDate ld2 = ld1.withYear(2019);
		LocalDate ld3 = ld2.with(ChronoField.MONTH_OF_YEAR, 8);
		LocalDate ld4 = ld3.withDayOfMonth(18);
		System.out.println(ld4);
		
		LocalDate _ld1 = LocalDate.of(2018, 6, 17);
		LocalDate _ld2 = _ld1.plusYears(1);
		LocalDate _ld3 = _ld2.minus(-2, ChronoUnit.MONTHS);
		LocalDate _ld4 = _ld3.plusWeeks(1);
		System.out.println(_ld4);
		
		// 使用TemporalAdjusters
		LocalDate $ld = LocalDate.of(2018, 6, 17);
		LocalDate $ld1 = $ld.with(nextOrSame(DayOfWeek.SUNDAY));
		LocalDate $ld2 = $ld.with(lastDayOfMonth());
		System.out.println($ld1);
		System.out.println($ld2);
		
		// 实现当天的下一个工作日日期，周六周日排除
		TemporalAdjuster ofDateAdjuster = TemporalAdjusters.ofDateAdjuster(date -> {
			// 获取当天是星期几
			int weekDay = date.get(ChronoField.DAY_OF_WEEK);
			// 定义增加天数的变量
			int addDays = 1;
			if(weekDay == 5) {
				addDays = 3;
			} else if(weekDay == 6) {
				addDays = 2;
			}
			return date.plus(addDays, ChronoUnit.DAYS);
		});
		LocalDate testDay = LocalDate.of(2018, 6, 15);
		LocalDate nextWorkingDay = testDay.with(ofDateAdjuster);
		System.out.println(nextWorkingDay);
		
		// 解析和格式化日期
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localNow = LocalDateTime.now();
		String formatTime = localNow.format(timeFormatter);
		LocalDateTime parseLocalTime = LocalDateTime.parse(formatTime, timeFormatter);
		System.out.println(formatTime);
		System.out.println(parseLocalTime);
		
		// 自定义DateTimeFormatter
		DateTimeFormatter myDateFormat = new DateTimeFormatterBuilder()
										 .appendText(ChronoField.YEAR)
										 .appendLiteral("年")
										 .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.NARROW)
										 .appendLiteral("月")
										 .appendText(ChronoField.DAY_OF_MONTH)
										 .appendLiteral("日   ")
										 .appendText(ChronoField.HOUR_OF_DAY)
										 .appendLiteral("时")
										 .appendText(ChronoField.MINUTE_OF_HOUR)
										 .appendLiteral("分")
										 .appendText(ChronoField.SECOND_OF_MINUTE)
										 .appendLiteral("秒")
										 .toFormatter();
		String format = LocalDateTime.now().format(myDateFormat);
		System.out.println(format);
		
		
	}
	
}
