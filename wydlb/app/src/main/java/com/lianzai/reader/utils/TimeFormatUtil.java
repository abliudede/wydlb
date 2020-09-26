package com.lianzai.reader.utils;

/**
 * Created by lrz on 16/3/4.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatUtil {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";



    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


//    public static String getInterval(String timesamp){
//        Date date = new Date(Long.parseLong(timesamp)*1000);
//        long delta = new Date().getTime() - date.getTime();
//        if (delta==0){
//            return "刚刚";
//        }
//        else if (delta < 1L * ONE_MINUTE) {
//            long seconds = toSeconds(delta);
//            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
//        }
//        else if (delta < 45L * ONE_MINUTE) {
//            long minutes = toMinutes(delta);
//            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
//        }
//        else if (delta < 24L * ONE_HOUR) {
//            long hours = toHours(delta);
//            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
//        }
//        else if (delta < 48L * ONE_HOUR) {
//            return "昨天"+getFormatTime(date, "HH:mm");
//        }else if (delta < 72L * ONE_HOUR) {
//            return "前天"+getFormatTime(date, "HH:mm");
//        }
//        else  if (delta < 7L * ONE_DAY) {
//            long days = toDays(delta);
//            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
//        }else  if (delta < 30L * ONE_DAY) {//显示月份日期
//            return  getFormatTime(date, "MM-dd HH:mm");
//        }else{
//            return  getFormatTime(date, "yyyy-MM-dd HH:mm");
//        }
//    }

    //把时间换成描述字符串，用于显示解禁时间
    public static String getFormatTimeStr(Long sec){
        int hour = 0;
        int min = 0;
        if(sec > 3600){
            hour = (int) (sec/3600);
        }
        min = (int) ((sec - hour*3600)/60);
        StringBuilder timestr = new StringBuilder();
        if(hour > 0){
            timestr.append(hour);
            timestr.append("小时");
        }
        timestr.append(min);
        timestr.append("分钟");
        timestr.append("后自动解除禁言");

        return timestr.toString();
    }

    /*
     * 获取XX天XX时格式
     * */
    public static String getFormatTimeDHM(Long sec){
        int day = 0;
        int hour = 0;
        int min = 0;
        long secTemp = sec;

        if(secTemp > 86400){
            day = (int) (secTemp/86400);
            secTemp = secTemp - day*86400;
        }


        if(secTemp > 3600){
            hour = (int) ( secTemp /3600);
            secTemp = secTemp - hour*3600;
        }


        if(secTemp > 60){
            min = (int) (secTemp/60);
        }

        StringBuilder timestr = new StringBuilder();
        timestr.append("听书有效时长：");
        if(day > 0){
            timestr.append(day);
            timestr.append("天");
        }
        if(day > 0 || hour > 0){
            timestr.append(hour);
            timestr.append("小时");
        }

        timestr.append(min);
        timestr.append("分钟");

        return timestr.toString();
    }


//    public static String getInterval(long timesamp) {
//        // 定义最终返回的结果字符串。
//        String interval = null;
//        Date createAt = new Date(timesamp);
//        Date nowDate=new Date();
//        long time1=nowDate.getTime();
//        long time2=createAt.getTime();
//
//        long millisecond =time1-time2;
//        long second = millisecond / 1000;
//        long currentHour=  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        long hour = (second / 60) / 60;
//
//        if (second <= 0) {
//            second = 0;
//        }
//        int days=differentDays(createAt,nowDate);
//        int years=differentYears(createAt,nowDate);
//
//        RxLogTool.e("getInterval"," days:"+days);
//        if (days<1){//同一天类 显示XX小时前
//            if (second == 0) {
//                interval = "刚刚";
//            } else if (second < 30) {
//                interval = second + "秒以前";
//            } else if (second >= 30 && second < 60) {
//                interval = "半分钟前";
//            } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
//                long minute = (second) / 60;
//                interval = minute + "分钟前";
//            }
//            else if (hour-currentHour<=0) {
//                interval = hour + "小时前";
//            }
//        }else if (days<2){//跨天在一天的情况 显示昨天
//            interval = "昨天" + getFormatTime(createAt, "HH:mm");
//        }else if (days<32){// 两天~31天  显示XX天前
//            interval = days+"天前";
//        }else if (days<366*2){// 半年内 显示月份+时间-开始判断是否跨年
//            if (years==1){
//                interval ="去年"+ getFormatTime(createAt, "MM月dd日 HH:mm");
//            }else if (years==2){
//                interval ="前年"+ getFormatTime(createAt, "MM月dd日 HH:mm");
//            }else{
//                interval =getFormatTime(createAt, "yyyy年MM月dd日 HH:mm");
//            }
//
//        }else {
//            interval = getFormatTime(createAt, "yyyy年MM月dd日 HH:mm");
//        }
//
//        return interval;
//    }


    public static String getIntervalOther(long timesamp) {
        // 定义最终返回的结果字符串。
        String interval = null;
        Date createAt = new Date(timesamp);
        Date nowDate=new Date();
        int days=differentDays(createAt,nowDate);

        RxLogTool.e("timesamp"," timesamp:"+timesamp);
        RxLogTool.e("getInterval"," days:"+days);
        if (days<1){//同一天类 显示XX小时前
            interval = "今天" ;
        }else if (days<2){//跨天在一天的情况 显示昨天
            interval = "昨天" ;
        }else {
            interval = getFormatTime(createAt, "yyyy-MM-dd");
        }
        return interval;
    }

    public static String getInterval(String timesamp) {
        // 定义最终返回的结果字符串。
        String interval = null;
        long time=0;
        try{
            time=Long.parseLong(timesamp);//防止后台数据返回异常而解析报错
        }catch (Exception e){

        }
        Date createAt = new Date(time);
        Date nowDate=new Date();
        long time1=nowDate.getTime();
        long time2=createAt.getTime();



        long millisecond =time1-time2;
        long second = millisecond / 1000;
        long currentHour=  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        long hour = (second / 60) / 60;

        if (second <= 0) {
            second = 0;
        }
        int days=differentDays(createAt,nowDate);
        int years=differentYears(createAt,nowDate);

        RxLogTool.e("getInterval"," days:"+days);
        if (days<1){//同一天类 显示XX小时前
            if (second == 0) {
                interval = "刚刚";
            } else if (second < 30) {
                interval = second + "秒以前";
            } else if (second >= 30 && second < 60) {
                interval = "半分钟前";
            } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
                long minute = (second) / 60;
                interval = minute + "分钟前";
            }
            else if (hour-currentHour<=0) {
                interval = hour + "小时前";
            }
        }else if (days<2){//跨天在一天的情况 显示昨天
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        }else if (days<32){// 两天~31天  显示XX天前
            interval = days+"天前";
        }else if (days<366*2){// 半年内 显示月份+时间-开始判断是否跨年
            if (years==1){
                interval ="去年"+ getFormatTime(createAt, "MM月dd日 HH:mm");
            }else if (years==2){
                interval ="前年"+ getFormatTime(createAt, "MM月dd日 HH:mm");
            }else{
                interval =getFormatTime(createAt, "MM月dd日 HH:mm");
            }

        }else {
            interval = getFormatTime(createAt, "yyyy年MM月dd日 HH:mm");
        }

        return interval;
    }
    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }

    public static String getJoinDays(String timesamp) {
        // 定义最终返回的结果字符串。
        String interval = null;
        long time=0;
        try{
            time=Long.parseLong(timesamp);//防止后台数据返回异常而解析报错
        }catch (Exception e){

        }
        Date createAt = new Date(time);
        Date nowDate=new Date();

        int days=differentDays(createAt,nowDate);

        return String.valueOf(days + 1);
    }


    /**
     * 判断两个时间相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //不同年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }
        else    //同一年
        {
            return day2-day1;
        }
    }


    public static int differentYears(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        return year2-year1;
    }

}

