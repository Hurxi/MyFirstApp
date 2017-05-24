package com.example.myfirstapp.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by 若希 on 2017/5/19.
 */

public class DateString {
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWeek;
    public static String StringDate(){
        final Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear=String.valueOf(c.get(Calendar.YEAR));

        mMonth=String.valueOf(c.get(Calendar.MONTH)+1);
        mDay=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        mWeek=String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWeek)){
            mWeek="日";
        }
        else if ("2".equals(mWeek)){
            mWeek="一";
        }
        else if ("3".equals(mWeek)){
            mWeek="二";
        }
        else if ("4".equals(mWeek)){
            mWeek="三";
        }
        else if ("5".equals(mWeek)){
            mWeek="四";
        }
        else if ("6".equals(mWeek)){
            mWeek="五";
        }
        else if ("7".equals(mWeek)){
            mWeek="六";
        }
        return mYear+","+mMonth+","+mDay+","+mWeek;
    }

}
