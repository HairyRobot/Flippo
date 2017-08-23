package com.f.Flippo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

	public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

	private DateUtil() {
	}

	/**
	 * Return string of today in specified format.
	 *
	 * @param format in SimpleDateFormat
	 */
	public static String today(String format) {
		return dateFormat(new Date(), format);
	}

	/**
	 * Return string of inputed date in specified format.
	 *
	 * @param date
	 * @param format in SimpleDateFormat
	 */
	public static String dateFormat(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
		return simpleDateFormat.format(date);
	}

	/**
	 * Return date of inputed date string in specified format.
	 *
	 * @param dateString
	 * @param format     in SimpleDateFormat
	 */
	public static Date stringToDate(String dateString, String format) {
		Date date = null;

		try {
			DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * Return date + days.
	 *
	 * @param date
	 * @param days Number of days to add
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (days >= 0) {
			while (days > 365) {
				cal.add(Calendar.DATE, 365);
				days = days - 365;
			}

			cal.add(Calendar.DATE, days);
		} else {
			while (days < 365) {
				cal.add(Calendar.DATE, -365);
				days = days + 365;
			}

			cal.add(Calendar.DATE, days);
		}

		return cal.getTime();
	}
}
