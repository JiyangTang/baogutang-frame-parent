package com.baogutang.frame.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 时间计算工具类
 */
public class DateUtil {
 
    /**
     * the milli second of a day
     */
    public static final long DAYMILLI = 24 * 60 * 60 * 1000;
    /**
     * the milli seconds of an hour
     */
    public static final long HOURMILLI = 60 * 60 * 1000;
    /**
     * the milli seconds of a minute
     */
    public static final long MINUTEMILLI = 60 * 1000;
    /**
     * the milli seconds of a second
     */
    public static final long SECONDMILLI = 1000;
    /**
     * added time
     */
    public static final String TIMETO = " 23:59:59";
    /**
     * flag before
     */
    public static final transient int BEFORE = 1;
    /**
     * flag after
     */
    public static final transient int AFTER = 2;
    /**
     * flag equal
     */
    public static final transient int EQUAL = 3;
    /**
     * date format dd/MMM/yyyy:HH:mm:ss +0900
     */
    public static final String TIME_PATTERN_LONG = "dd/MMM/yyyy:HH:mm:ss +0900";
    /**
     * date format dd/MM/yyyy:HH:mm:ss +0900
     */
    public static final String TIME_PATTERN_LONG2 = "dd/MM/yyyy:HH:mm:ss +0900";
    /**
     * date format YYYY-MM-DD HH24:MI:SS
     */
    public static final String DB_TIME_PATTERN = "YYYY-MM-DD HH24:MI:SS";
    /**
     * date format YYYYMMDDHH24MISS
     */
    public static final String DB_TIME_PATTERN_1 = "YYYYMMDDHH24MISS";
    /**
     * date format dd/MM/yy HH:mm:ss
     */
    public static final String TIME_PATTERN_SHORT = "dd/MM/yy HH:mm:ss";
    /**
     * date format dd/MM/yy HH24:mm
     */
    public static final String TIME_PATTERN_SHORT_1 = "yyyy/MM/dd HH:mm";
    /**
     * date format yyyyMMddHHmmss
     */
    public static final String TIME_PATTERN_SESSION = "yyyyMMddHHmmss";
    /**
     * date format yyyyMMddHHmmssSSS
     */
    public static final String TIME_PATTERN_MILLISECOND = "yyyyMMddHHmmssSSS";
    /**
     * date format yyyyMMdd
     */
    public static final String DATE_FMT_0 = "yyyyMMdd";
    /**
     * date format yyyy/MM/dd
     */
    public static final String DATE_FMT_1 = "yyyy/MM/dd";
    /**
     * date format yyyy/MM/dd hh:mm:ss
     */
    public static final String DATE_FMT_2 = "yyyy/MM/dd hh:mm:ss";
    /**
     * date format yyyy-MM-dd
     */
    public static final String DATE_FMT_3 = "yyyy-MM-dd";
    /**
     * date format yyyy-MM-dd HH
     */
    public static final String DATE_FMT_5 = "yyyy-MM-dd HH";
    /**
     * date format yyyy-MM
     */
    public static final String DATE_FMT_6 = "yyyy-MM";
    /**
     * date format MM月dd日 HH:mm
     */
    public static final String DATE_FMT_8 = "HH:mm:ss";
    /**
     * date format MM月dd日 HH:mm
     */
    public static final String DATE_FMT_9 = "yyyy.MM.dd";
    public static final String DATE_FMT_10 = "HH:mm";
    public static final String DATE_FMT_11 = "yyyy.MM.dd HH:mm:ss";
    public static final String DATE_FMT_14 = "yyyyMM";
    public static final String DATE_FMT_15 = "MM-dd HH:mm:ss";
    public static final String DATE_FMT_16 = "yyyyMMddHHmm";
    public static final String DATE_FMT_17 = "HHmmss";
    public static final String DATE_FMT_18 = "yyyy";

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FMT_19 = "HH:mm:ss dd-MM-yyyy ";

    public static final String DATE_FMT_20 = "dd-MM-yyyy";
    
    public static final String DATE_FMT_21 = "dd/MM/yyyy HH:mm:ss";
    
    public static final String DATE_FMT_22 = "dd/MM/yyyy";
 
    public static final String DATE_FMT_23 = "dd-MM-yyyy HH:mm:ss";
    
    public static final String DATE_MATCH_1="\\d{2}-\\d{2}-\\d{4}|\\d{2}/\\d{2}/\\d{4}";
    /**
     * localDateTime 转 自定义格式string
     *
     * @param localDateTime
     * @param format        例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String formatLocalDateTimeToString(LocalDateTime localDateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
 
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * string 转 LocalDateTime
     *
     * @param dateStr 例："2017-08-11 01:00:00"
     * @param format  例："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        if(dateStr != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将String时间转换成毫秒
     * @param dateStr 例：2020-05-25 16:12:23
     * @param format  例：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Long stringToMillisecond(String dateStr,String format) {
        Long timeInMillis = null;

        if(dateStr != null && !dateStr.equals("")) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat(format).parse(dateStr));
                timeInMillis = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeInMillis;
    }

    /**
     * 将string类型的毫秒，转换成日期格式
     * @param dateStr 例：1590393922308
     * @param format  例：yyyy-MM-dd HH:mm:ss
     * @return 例： 2020-05-25 16:05:22
     */
    public static String stringMsToDatetime(String dateStr,String format) {
        String resTime = "";
        if(dateStr != null && dateStr.length() > 0){
            Timestamp timestamp = new Timestamp(new Long(dateStr));
            resTime = formatLocalDateTimeToString(timestampToLocalDateTime(timestamp),format);
        }
        return resTime;
    }



    public static Date stringToDateTime(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            Date date = Date.from(instant);
            
            return date;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
 
 
    /**
     * 根据时间获取当月有多少天数
     *
     * @param date
     * @return
     */
    public static int getActualMaximum(Date date) {
 
        return dateToLocalDateTime(date).getMonth().length(dateToLocalDate(date).isLeapYear());
    }
 
    /**
     * 根据日期获得星期
     *
     * @param date
     * @return 1:星期一；2:星期二；3:星期三；4:星期四；5:星期五；6:星期六；7:星期日；
     */
    public static int getWeekOfDate(Date date) {
        return dateToLocalDateTime(date).getDayOfWeek().getValue();
    }
 
 
    /**
     * 计算两个日期LocalDate相差的天数，不考虑日期前后，返回结果>=0
     *
     * @param before
     * @param after
     * @return
     */
    public static int getAbsDateDiffDay(LocalDate before, LocalDate after) {
 
        return Math.abs(Period.between(before, after).getDays());
    }
 
    /**
     * 计算两个时间LocalDateTime相差的天数，不考虑日期前后，返回结果>=0
     *
     * @param before
     * @param after
     * @return
     */
    public static int getAbsTimeDiffDay(LocalDateTime before, LocalDateTime after) {
 
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getDays());
    }
 
    /**
     * 计算两个时间LocalDateTime相差的月数，不考虑日期前后，返回结果>=0
     *
     * @param before
     * @param after
     * @return
     */
    public static int getAbsTimeDiffMonth(LocalDateTime before, LocalDateTime after) {
 
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getMonths());
    }
 
    /**
     * 计算两个时间LocalDateTime相差的年数，不考虑日期前后，返回结果>=0
     *
     * @param before
     * @param after
     * @return
     */
    public static int getAbsTimeDiffYear(LocalDateTime before, LocalDateTime after) {
 
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getYears());
    }
 
 
    /**
     * 根据传入日期返回星期几
     *
     * @param date 日期
     * @return 1-7 1：星期天,2:星期一,3:星期二,4:星期三,5:星期四,6:星期五,7:星期六
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
 
 
    /**
     * 获取指定日期的当月的月份数
     *
     * @param date
     * @return
     */
    public static int getLastMonth(Date date) {
        return dateToLocalDateTime(date).getMonth().getValue();
 
    }
 
 
    /**
     * 特定日期的当月第一天
     *
     * @param date
     * @return
     */
    public static LocalDate newThisMonth(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }
 
    /**
     * 特定日期的当月最后一天
     *
     * @param date
     * @return
     */
    public static LocalDate lastThisMonth(Date date) {
        int lastDay = getActualMaximum(date);
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), lastDay);
    }
 
 
    /**
     * 特定日期的当年第一天
     *
     * @param date
     * @return
     */
    public static LocalDate newThisYear(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), 1, 1);
 
    }
 
 
    public static Timestamp getCurrentDateTime() {
        return new Timestamp(Instant.now().toEpochMilli());
 
    }
 
    /**
     * 获取当前时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
 
    }
 
 
    /**
     * 修改日期时间的时间部分
     *
     * @param date
     * @param customTime 必须为"hh:mm:ss"这种格式
     */
    public static LocalDateTime reserveDateCustomTime(Date date, String customTime) {
        String dateStr = dateToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }
 
    /**
     * 修改日期时间的时间部分
     *
     * @param date
     * @param customTime 必须为"hh:mm:ss"这种格式
     */
    public static LocalDateTime reserveDateCustomTime(Timestamp date, String customTime) {
        String dateStr = timestampToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }
 
    /**
     * 把日期后的时间归0 变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param date
     * @return LocalDateTime
     */
    public static final LocalDateTime zerolizedTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0, 0);
 
    }
 
    /**
     * 把时间变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime zerolizedTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0, 0);
    }
 
    /**
     * 把日期的时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999 * 1000000);
    }
 
    /**
     * 把时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getEndTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999 * 1000000);
    }
 
    /**
     * 计算特定时间到 当天 23.59.59.999 的秒数
     *
     * @return
     */
    public static int calculateToEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime end = getEndTime(date);
        return (int) (end.toEpochSecond(ZoneOffset.UTC) - localDateTime.toEpochSecond(ZoneOffset.UTC));
    }
 
 
    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param localDateTime 例：ChronoUnit.DAYS
     * @param chronoUnit
     * @param num
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(LocalDateTime localDateTime, ChronoUnit chronoUnit, int num) {
        return localDateTime.plus(num, chronoUnit);
    }
 
    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param chronoUnit 例：ChronoUnit.DAYS
     * @param num
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(Date date, ChronoUnit chronoUnit, int num) {
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime.plus(num, chronoUnit);
    }
 
    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param chronoUnit 例：ChronoUnit.DAYS
     * @param num
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(Timestamp date, ChronoUnit chronoUnit, int num) {
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime.plus(num, chronoUnit);
    }
 
    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
 
        return localDateTime;
    }
 
    /**
     * Timestamp 转 LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(Timestamp date) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, date.getNanos(), ZoneOffset.of("+8"));
 
        return localDateTime;
    }
 
    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
 
        return dateToLocalDateTime(date).toLocalDate();
    }
 
    /**
     * timestamp 转 LocalDateTime
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate timestampToLocalDate(Timestamp date) {
 
        return timestampToLocalDateTime(date).toLocalDate();
    }
 
    /**
     * 比较两个LocalDateTime是否同一天
     *
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameDay(LocalDateTime begin, LocalDateTime end) {
        return begin.toLocalDate().equals(end.toLocalDate());
    }
 
 
    /**
     * 比较两个时间LocalDateTime大小
     *
     * @param time1
     * @param time2
     * @return 1:第一个比第二个大；0：第一个与第二个相同；-1：第一个比第二个小
     */
    public static int compareTwoTime(LocalDateTime time1, LocalDateTime time2) {
 
        if (time1.isAfter(time2)) {
            return 1;
        } else if (time1.isBefore(time2)) {
            return -1;
        } else {
            return 0;
        }
    }
 
 
    /**
     * 比较time1,time2两个时间相差的秒数，如果time1<=time2,返回0
     *
     * @param time1
     * @param time2
     */
    public static long getTwoTimeDiffSecond(Timestamp time1, Timestamp time2) {
        long diff = timestampToLocalDateTime(time1).toEpochSecond(ZoneOffset.UTC) - timestampToLocalDateTime(time2).toEpochSecond(ZoneOffset.UTC);
        if (diff > 0) {
            return diff;
        } else {
            return 0;
        }
    }
 
    /**
     * 比较time1,time2两个时间相差的分钟数，如果time1<=time2,返回0
     *
     * @param time1
     * @param time2
     */
    public static long getTwoTimeDiffMin(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 60;
        if (diff > 0) {
            return diff;
        } else {
            return 0;
        }
    }
 
    /**
     * 比较time1,time2两个时间相差的小时数，如果time1<=time2,返回0
     *
     * @param time1
     * @param time2
     */
    public static long getTwoTimeDiffHour(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 3600;
        if (diff > 0) {
            return diff;
        } else {
            return 0;
        }
    }
 
    /**
     * 判断当前时间是否在时间范围内
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isTimeInRange(Date startTime, Date endTime) throws Exception {
        LocalDateTime now = getCurrentLocalDateTime();
        LocalDateTime start = dateToLocalDateTime(startTime);
        LocalDateTime end = dateToLocalDateTime(endTime);
        return (start.isBefore(now) && end.isAfter(now)) || start.isEqual(now) || end.isEqual(now);
    }
 
	/**
	 * 将时间转换为时间戳
	 * 13位截取返回10位
	 */
	public static String dateToStamp(String s) throws Exception {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime() / 1000;
		res = String.valueOf(ts);
		return res;
	}

	/**
	 * 将时间戳转换为时间
	 * 10位时间戳补齐13位
	 */
	public static String stampToDate(String s) {
		if (StringUtils.isNotBlank(s) && s.length() == 10) {
			s = s + "000";
		}
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
    
    /**
     * 当前时间 转 自定义格式string
     * @param format 例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String nowTimeToString(String format) {
        try {
        	LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
 
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 当前时间 转 自定义格式string
     * @param format 例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String uTC8ToString(String format) {
        try {
        	LocalDateTime localDateTime = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);

        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当前时间 加减毫秒值转 自定义格式string
     * @param type 1 :加， 2：减
     * @param seconds 秒值
     * @param format 例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String changeNowTimeToString(String type, Long seconds, String format) {
        try {
        	LocalDateTime localDateTime = null;
        	if ("2".equals(type)) {
            	localDateTime = LocalDateTime.now().minusSeconds(seconds);
			} else {
            	localDateTime = LocalDateTime.now().plusSeconds(seconds);
			}
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
 
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 时间 转 自定义格式string
     * @param format 例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String dateToString(Date date, String format) {
        try {
        	SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
 
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 时间 转 时间戳
     * @param time 例：yyyy-MM-dd hh:mm:ss
     * @return
     * @throws Exception 
     */
    public static Long timeToStamp(String time) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_PATTERN);
		Date date = simpleDateFormat.parse(time);
		long ts = date.getTime() / 1000;
		return ts;
    }
    
    /**
     * 时间 转 时间戳
     * @param time 例：yyyy-MM-dd hh:mm:ss
     * @return
     * @throws Exception
     */
    public static Long timeToStamp(String time, String format) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(time);
		long ts = date.getTime() / 1000;
		return ts;
    }

	/**
	 * 将时间戳转换为时间
	 * 10位时间戳补齐13位
	 */
	public static String stampToFormatDate(String s, String format) {
		if (StringUtils.isNotBlank(s) && s.length() == 10) {
			s = s + "000";
		}
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
    
    
    /**
     * 计算两个时间LocalDate相差的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static int getDayDiff(LocalDate before, LocalDate after) {
        return (int) (after.toEpochDay() - before.toEpochDay());
    }
    
    /**
     * 时间格式字符串格式间转换
     *
     * @param formatBefore
     * @param formatAfter
     * @return
     */
    public static String dateStrToDateStr(String formatBefore, String formatAfter, String time) {
    	String dateStr = null;
    	try {
        	Date date = new SimpleDateFormat(formatBefore).parse(time);
        	dateStr = new SimpleDateFormat(formatAfter).format(date);
		} catch (Exception e) {
			dateStr = time;
		}
        return dateStr;
    }
    

    /**
     * 时间格式字符串相差 如果dateStr1 <= dateStr1,返回0
     * @param dateStr1 
     * @param dateStr1 
     * @param type  second秒，minute分， hour时
     * @param dateFormat
     * @return 
     */
    public static Long getTwoDateStrDiff(String dateStr1, String dateStr2, String type, String dateFormat) {
    	SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
    	Long diff = 0l;
    	try {
			Date date1 = sf.parse(dateStr1);
			Timestamp time1 = new Timestamp(date1.getTime());
	    	Date date2 = sf.parse(dateStr2);
			Timestamp time2 = new Timestamp(date2.getTime());
	    	if ("second".equalsIgnoreCase(type)) {
	    		diff = getTwoTimeDiffSecond(time1, time2);
			} else if ("minute".equalsIgnoreCase(type)) {
				diff = getTwoTimeDiffMin(time1, time2);
			} else if ("hour".equalsIgnoreCase(type)) {
				diff = getTwoTimeDiffHour(time1, time2);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return diff;
    }
    
	public static Date parse(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(strDate);
	}
	
	public static Date parse(String strDate, String format) throws ParseException {
	    try {
//	        SimpleDateFormat sdf = new SimpleDateFormat(format);
//	        return sdf.parse(strDate);
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
	        LocalDateTime dt2 = LocalDateTime.parse(strDate, dtf);
	        ZonedDateTime zdt = dt2.atZone(ZoneId.systemDefault());//Combines this date-time with a time-zone to create a  ZonedDateTime.
	        Date date = Date.from(zdt.toInstant());
	        return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
    
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) { // 出生日期晚于当前时间，无法计算
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR); // 当前年份
		int monthNow = cal.get(Calendar.MONTH); // 当前月份
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); // 当前日期
		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth; // 计算整岁数
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;// 当前日期在生日之前，年龄减一
                }
			} else {
				age--;// 当前月份在生日之前，年龄减一

			}
		}
		return age;
	}
    

	public static int getYearAge(String yearBirth)  {
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR); // 当前年份
		int age = yearNow - Integer.parseInt(yearBirth) + 1; // 计算整岁数
		return age;
	}
    
	public static String getYearBirth(int age)  {
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR); // 当前年份
		int yearBirth = yearNow - age + 1; // 计算出生年份
		return String.valueOf(yearBirth);
	}

	/**
	 * 时间格式化
	 */
	public static String dateFormatToString(Date date, String format) {
		if (null == date || null == format) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}


    /**
     * 获取今天的日期
     * @return
     */
    public static String getToday(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * yyyyMMddHHmmss
     * 获取今天的日期
     * @return
     */
    public static String getTodayYMDHMS(){
        return new SimpleDateFormat(TIME_PATTERN_SESSION).format(new Date());
    }

	/**
	 * 获取当前日期前一天
	 */
	public static String getDatebeforeToday(Date date) {
	 if (date!=null) {
		  date=new Date();
	     }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.set(Calendar.DATE, cld.get(Calendar.DATE)-1);
		 sdf.format(cld.getTime());
		System.out.println( sdf.format(cld.getTime()));
		return sdf.format(cld.getTime());
	}
	
	/**
	 * 根据时间戳获取年月日
	 */
	public static Map<String, String> getYearAndMonthAndDay(Long time) {
		Map<String, String> map=new HashMap<String, String>();
		if (null == time) {
			return map;
		}
		String dateStr = String.valueOf(time);
		if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 10) {
			dateStr = dateStr + "000";
			time = Long.valueOf(dateStr);
		}
		if (time!=null) {
			Date date=new Date(time);
			SimpleDateFormat yy = new SimpleDateFormat("yyyy");
			SimpleDateFormat  mm= new SimpleDateFormat("MM");
		    SimpleDateFormat dd= new SimpleDateFormat("dd");
			String year = yy.format(date);
			String month = mm.format(date);
			String day = dd.format(date);
			map.put("year", year);
			map.put("month", month);
			map.put("day", day);
		}
		return map;
	}

	/**
	 * 根据时间戳获取年月日
	 */
	public static Map<String, String> getYearAndMonthAndDay(Date date) {
		Map<String, String> map=new HashMap<String, String>();
		if (null == date) {
			return map;
		}

		SimpleDateFormat yy = new SimpleDateFormat("yyyy");
		SimpleDateFormat  mm= new SimpleDateFormat("MM");
	    SimpleDateFormat dd= new SimpleDateFormat("dd");
		String year = yy.format(date);
		String month = mm.format(date);
		String day = dd.format(date);
		map.put("year", year);
		map.put("month", month);
		map.put("day", day);

		return map;
	}

    public static double division(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	/**
	 * 匹配越南日期格式
	 * @param dateTime
	 * @return
	 */
	public static Boolean matchYNTime(String dateTime) {
	    Pattern word = Pattern.compile(DATE_MATCH_1);
        Matcher match = word.matcher(dateTime);
        if(match.find()) {
            return true;
        }
	    return false;
	}
	
	
	
    /**
     * 增加或减少几天的，获取改天零点的毫秒值
     */
    public static Long getNowDiffTime(int num) {
    	Long time = null;
    	try {
    		Calendar calendar1 = Calendar.getInstance();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    		calendar1.add(Calendar.DATE, num);
    		String three_days_ago = sdf1.format(calendar1.getTime());
    		Date date = sdf1.parse(three_days_ago);
    		time = date.getTime() / 1000;
		} catch (Exception e) {

		}
    	return time;
    }

    public static Date convert(String str, String format) {
        if (StringUtils.isNotBlank(str)) {
            try {
                return new SimpleDateFormat(format).parse(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	
}
