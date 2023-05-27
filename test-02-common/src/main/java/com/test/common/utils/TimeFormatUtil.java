package com.test.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liugc
 * @desc [事件预处理方案配置接口]
 * @phone 17805131927
 * @email liu.gc@asiainfo-sec.com
 * @time 2022-03-30 19:50:00
 */
@Slf4j
public class TimeFormatUtil {

    private static SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf03 = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 时间计算标记 1-按秒加减
     */
    public static final int TIME_CALCULATION_FLAG_SECOND = 1;
    /**
     * 时间计算标记 2-按分钟加减
     */
    public static final int TIME_CALCULATION_FLAG_MINUTE = 2;
    /**
     * 时间计算标记 3-按小时加减
     */
    public static final int TIME_CALCULATION_FLAG_HOUR = 3;
    /**
     * 时间计算标记 4-按天加减
     */
    public static final int TIME_CALCULATION_FLAG_DAY_OF_MONTH = 4;
    /**
     * 时间计算标记 5-按月加减
     */
    public static final int TIME_CALCULATION_FLAG_MONTH = 5;
    /**
     * 时间计算标记 6-按年加减
     */
    public static final int TIME_CALCULATION_FLAG_YEAR = 6;

    /**
     * 字符串类型的时间实现按天、小时进行加减计算
     * @param dStr  需要加减的时间
     * @param flag  加减的类型，1：按天加减；2：按小时加减
     * @param ds    加减的数量
     * @return
     */
    public static String timeCalculation(String dStr,int flag,int ds) {
        String result = "";
        try {
            Date pd = sdf01.parse(dStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pd);
            if (flag == TIME_CALCULATION_FLAG_SECOND) {
                calendar.add(Calendar.SECOND, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 秒之后：" + sdf01.format(calendar.getTime()));
            } else if (flag == TIME_CALCULATION_FLAG_MINUTE) {
                calendar.add(Calendar.MINUTE, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 分钟之后：" + sdf01.format(calendar.getTime()));
            }  else if (flag == TIME_CALCULATION_FLAG_HOUR) {
                calendar.add(Calendar.HOUR, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 小时之后：" + sdf01.format(calendar.getTime()));
            } else if (flag == TIME_CALCULATION_FLAG_DAY_OF_MONTH) {
                calendar.add(Calendar.DAY_OF_MONTH, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 天之后：" + sdf01.format(calendar.getTime()));
            } else if (flag == TIME_CALCULATION_FLAG_MONTH) {
                calendar.add(Calendar.MONTH, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 月之后：" + sdf01.format(calendar.getTime()));
            } else if (flag == TIME_CALCULATION_FLAG_YEAR) {
                calendar.add(Calendar.YEAR, ds);
                log.info("原时间：" + dStr + "，增加 " + ds + " 年之后：" + sdf01.format(calendar.getTime()));
            }

            result = sdf01.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 两个字符串类型的时间进行比较
     * @param dStr1 时间1，格式：2021-11-19 11:32:05
     * @param dStr2 时间2，格式：2021-11-19 11:35:05
     * @return  如果 dStr1 > dStr2 ，则返回 true，否则返回 false
     */
    public static boolean dateCompare(String dStr1,String dStr2) {
        boolean result = false;
        try {
            Date pd1 = sdf01.parse(dStr1);
            Date pd2 = sdf01.parse(dStr2);
            int comRes = pd1.compareTo(pd2);
            if (comRes > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将字符串类型的时间或日期转换成毫秒值
     * @param tStr  将要格式化的时间或日期
     * @param formatStr 格式化成哪种格式，yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd
     * @return  返回毫秒值
     */
    public static long getMsByTime(String tStr,String formatStr) {
        long ts = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
            Date date = simpleDateFormat.parse(tStr);
            ts = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    /**
     * 将时间戳转换为标准时间格式的时间
     * @param ms    目标毫秒值
     * @param formatStr 目标时间格式
     * @return  字符串类型的时间
     */
    public static String getTimeByMs(long ms,String formatStr) {
        String reTime = null;
        try {
            Date date = new Date(ms);
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            reTime = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reTime;
    }

    public static void main(String[] args) throws ParseException {
        String timeByMs = getTimeByMs(1654071265 * 1000L, "yyyy-MM-dd HH:mm:ss");
        log.info(timeByMs);
    }

}
