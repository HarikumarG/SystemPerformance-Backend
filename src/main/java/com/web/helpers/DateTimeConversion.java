package com.web.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;

public class DateTimeConversion {

	public static String toUTC(String localdate) {
		String date = localdate.replace(' ','T');
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse(date,formatter);
		ZonedDateTime zoned = ZonedDateTime.of(ldt,ZoneId.of("Asia/Kolkata"));
		LocalDateTime localUTC = zoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
		String utc = localUTC.format(formatter);
		return utc;
	}

	public static String toLocal(String utcdate) {
		Instant time = Instant.parse(utcdate);
		ZonedDateTime zoned = time.atZone(ZoneId.of("Asia/Kolkata"));
		LocalDateTime localtime = zoned.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String local = localtime.format(formatter);
		return local;
	}
}