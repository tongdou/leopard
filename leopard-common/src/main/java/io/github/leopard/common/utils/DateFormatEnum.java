package io.github.leopard.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期格式化或解析工具
 *
 * @author pleuvoir
 */
public enum DateFormatEnum {
    /**
     * 紧凑的日期格式：yyyyMMdd
     */
    DATE_COMPACT("yyyyMMdd"),
    /**
     * 紧凑的日期时间格式：yyyyMMddHHmmss
     */
    DATETIME_COMPACT("yyyyMMddHHmmss"),
    /**
     * 紧凑的时间格式：HHmmss
     */
    TIME_COMPACT("HHmmss"),

    /**
     * 默认的日期格式：yyyy-MM-dd
     */
    DATE_DEFAULT("yyyy-MM-dd"),
    /**
     * 年月日 yyMMdd
     **/
    DATE_YEAR_DATE("yyMMdd"),
    /**
     * 默认的日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    DATETIME_DEFAULT("yyyy-MM-dd HH:mm:ss"),
    /**
     * 默认的时间格式：HH:mm:ss
     */
    TIME_DEFAULT("HH:mm:ss"),

    /**
     * 默认的不带秒的日期时间格式：yyyy-MM-dd HH:mm
     */
    DATETIME_DEFAULT_NONE_SECOND("yyyy-MM-dd HH:mm"),

    /**
     * 中文的日期格式：yyyy年MM月dd日
     */
    DATE_CHINESE("yyyy年MM月dd日"),
    /**
     * 中文的日期时间格式：yyyy年MM月dd日HH时mm分ss秒
     */
    DATETIME_CHINESE("yyyy年MM月dd日HH时mm分ss秒"),
    /**
     * 中文的时间格式：HH时mm分ss秒
     */
    TIME_CHINESE("HH时mm分ss秒");


    private String partten;
    private DateTimeFormatter formatter;

    private DateFormatEnum(String partten) {
        this.partten = partten;
        this.formatter = DateTimeFormatter.ofPattern(partten);
    }

    /**
     * 获取格式字符串
     *
     * @return
     */
    public String getPartten() {
        return this.partten;
    }

    /**
     * 获取{@link SimpleDateFormat}对象<br/>
     * <b>注意：</b>每次调用都会返回新的对象
     */
    public SimpleDateFormat get() {
        return new SimpleDateFormat(this.partten);
    }

    /**
     * 格式化一个{@link LocalDateTime}日期对象，参数为空时返回null
     */
    public String format(LocalDateTime datetime) {
        return datetime == null ? null : formatter.format(datetime);
    }

    /**
     * 格式化一个{@link LocalDate}日期对象，参数为空时返回null
     */
    public String format(LocalDate date) {
        return date == null ? null : formatter.format(date);
    }

    /**
     * 格式化一个{@link LocalTime}日期对象，参数为空时返回null
     *
     * @param time
     * @return
     */
    public String format(LocalTime time) {
        return time == null ? null : formatter.format(time);
    }

    /**
     * 格式化一个{@link Date}日期对象，参数为空时返回null
     */
    public String format(Date date) {
        return date == null ? null : get().format(date);
    }


    public String formatTimestamp(String timestamp) {
        final Date date = new Date(Long.parseLong(timestamp) * 1000);
        return format(date);
    }

    public LocalDateTime parseTimestampToLocalDatetime(String timestamp) {
        final String formatTimestamp = formatTimestamp(timestamp);
        return parseToDatetime(formatTimestamp);
    }

    /**
     * 转换字符串为{@link LocalDateTime}日期对象，参数为空或空字符串时返回null
     */
    public LocalDateTime parseToDatetime(String datetime) {
        return StringUtils.isBlank(datetime) ? null : LocalDateTime.parse(datetime, formatter);
    }


    /**
     * 转换字符串为{@link LocalDate}日期对象，参数为空或空字符串时返回null
     */
    public LocalDate parseToDate(String date) {
        return StringUtils.isBlank(date) ? null : LocalDate.parse(date, formatter);
    }

    /**
     * 转换字符串为{@link LocalTime}日期对象，参数为空或空字符串时返回null
     */
    public LocalTime parseToTime(String time) {
        return StringUtils.isBlank(time) ? null : LocalTime.parse(time, formatter);
    }

    /**
     * 转换字符串为{@link Date}日期对象，参数为空或空字符串时返回null
     */
    public Date parseToUtilDate(String time) throws ParseException {
        return StringUtils.isBlank(time) ? null : get().parse(time);
    }


    /**
     * 转换LocalDate或者LocalDateTime为{@link Date}日期对象，不符合格式则抛出异常
     */
    public Date parseToUtilDate(TemporalAccessor temporal) {
        Date date;
        try {
            date = parseToUtilDate(formatter.format(temporal));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return date;
    }

    public static Date getDateMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        return calendar.getTime();
    }

    public static Date getDateMaxTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        return calendar.getTime();
    }



    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (StringUtils.isEmpty(formats)) {
            formats = "yyyy-MM-dd HH:mm:ss";
        }
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }


    public static void main(String[] args) {
        System.out.println(TimeStamp2Date("1596531048", null));
    }
}
