package com.holtek.util;

/**
 * Created by holtek on 2016/7/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    private static final int MIDNIGHT_MINUTES = 1440;

    public static boolean expire1month(String paramString)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar localCalendar = Calendar.getInstance();
        Date localDate1 = null;
        Date localDate2 = null;
        try
        {
            localDate1 = localSimpleDateFormat.parse(getToday());
            localCalendar.setTime(localSimpleDateFormat.parse(paramString.substring(0, 10)));
            localCalendar.add(Calendar.DAY_OF_MONTH, 30);
            Date localDate3 = localSimpleDateFormat.parse(new SimpleDateFormat("yyyy-MM-dd").format(localCalendar.getTime()));
            localDate2 = localDate3;
        }
        catch (ParseException localParseException)
        {
            localParseException.printStackTrace();
        }
        return !localDate1.after(localDate2);
    }

    public static int getCurrentHour()
    {

        return new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentHourAndMinutes()
    {
        GregorianCalendar localGregorianCalendar = new GregorianCalendar();
        return 60 * localGregorianCalendar.get(Calendar.HOUR_OF_DAY) + localGregorianCalendar.get(Calendar.MINUTE);
    }

    public static int getCurrentMinute()
    {

        return new GregorianCalendar().get(Calendar.MINUTE);
    }

    public static String getCurrentTime()
    {
        Calendar localCalendar = Calendar.getInstance();
        int hour = localCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = localCalendar.get(Calendar.MINUTE);
        int second = localCalendar.get(Calendar.SECOND);
        //格式化时需注意本地化语言的差别
        return String.format(Locale.US,"%2d:%2d:%2d",hour,minute,second);
    }

    public static int getLastDay(int paramInt1, int paramInt2)
    {
        return new GregorianCalendar(paramInt1, paramInt2 - 1, 1).getActualMaximum(Calendar.DATE);
    }

    public static String getToday()
    {
        Calendar localCalendar = Calendar.getInstance();
        int year = localCalendar.get(Calendar.YEAR);
        int month = localCalendar.get(Calendar.MONTH)+1;
        int day = localCalendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.US,"%4d-%2d-%2d",year,month,day);
    }

    public static String getWeekDisplayString(Calendar paramCalendar)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = localSimpleDateFormat.format(paramCalendar.getTime());
        Calendar localCalendar = (Calendar)paramCalendar.clone();
        localCalendar.add(Calendar.DATE, 6);
        return String.format("%s ~ %s", new Object[] { str, localSimpleDateFormat.format(localCalendar.getTime()) });
    }

    public static List<Calendar> getWeeks(int paramInt1, int paramInt2)
    {
        //构造一个GregorianCalendar与给定日期的默认时区设置默认语言环境
        //paramInt1 年,paramInt2 月,1日
        GregorianCalendar localGregorianCalendar = new GregorianCalendar(paramInt1, paramInt2 - 1, 1);
        ArrayList localArrayList = new ArrayList();
        int i = 1 - localGregorianCalendar.get(Calendar.DAY_OF_WEEK);
        if (i != 0) {
            localGregorianCalendar.add(Calendar.DAY_OF_MONTH, i);
        }
        localArrayList.add((Calendar)localGregorianCalendar.clone());
        for (;;)
        {
            localGregorianCalendar.add(Calendar.DAY_OF_MONTH, 7);
            if (localGregorianCalendar.get(Calendar.MONTH) != paramInt2 - 1) {
                return localArrayList;
            }
            localArrayList.add((Calendar)localGregorianCalendar.clone());
        }
    }

    public static boolean isCurrentTimeContained(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        return isGivenTimeContained(getCurrentHourAndMinutes(), paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static boolean isGivenTimeContained(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
        int i = paramInt3 + paramInt2 * 60;
        int j = paramInt5 + paramInt4 * 60;
        if (j < i)
        {
            if (((paramInt1 < i) || (paramInt1 > 1440)) && ((paramInt1 < 1440) || (paramInt1 >= j))) {}
        }
        else {
            while ((paramInt1 >= i) && (paramInt1 < j)) {
                return true;
            }
        }
        return false;
    }

    public static String long2DateStr(long paramLong)
    {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(paramLong));
    }

    public static String long2Str(long paramLong)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(paramLong));
    }

    public static void main(String[] paramArrayOfString)
    {
        List localList = getWeeks(2011, 12);
        System.out.println("Month = 12");
        Iterator localIterator = localList.iterator();
        if (!localIterator.hasNext()) {
            return ;
        }
        for (int i = 0; i<12; i++) {
            System.out.println("Last day of " + (i + 1) + " = " + getLastDay(2011, i + 1));
        }
        Calendar localCalendar = (Calendar)localIterator.next();
        System.out.println(getWeekDisplayString(localCalendar));
    }

    public static long now2Long()
    {
        return Calendar.getInstance().getTime().getTime();
    }

    public static String now2Str()
    {
        Calendar localCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(localCalendar.getTime());
    }

    public static String now2StrInFileName()
    {
        Calendar localCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(localCalendar.getTime());
    }

    public static String nowDate2Str()
    {
        Calendar localCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(localCalendar.getTime());
    }

    public static String prev2DateStr(long paramLong)
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.MILLISECOND, (int)(-1L * paramLong));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(localCalendar.getTime());
    }

    public static String prev2Str(int paramInt)
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.MILLISECOND, paramInt * -1);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(localCalendar.getTime());
    }

    public static String sec2Str(int paramInt)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(1000L * paramInt));
    }

    public static long str2Long(String paramString)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try
        {
            long l = localSimpleDateFormat.parse(paramString).getTime();
            return l;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return -1L;
    }
}
