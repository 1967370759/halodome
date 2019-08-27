package run.halo.app.utils;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private DateUtils() {
    }

    /**
     * 获取当前日期
     * @return 获取当前日期
     */
    @NonNull
    public static Date now() {
        return new Date();
    }

    ;

    /**
     * 将日期转换成日历实例
     * @param date 实例不能为空
     * @return 日历实测
     */
    public static Calendar convertTo(@NonNull Date date) {
        Assert.notNull(date, "date 不能为空");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 添加日期
     * @param date 当前日期不能为null
     * @param time  不能为1
     * @param timeUnit 不能为空
     * @return 添加日期
     */
    public static Date add(@NonNull Date date, long time, @NonNull TimeUnit timeUnit) {
        Assert.notNull(date, "date不能为空");
        Assert.isTrue(time >= 0, "加法 time 不能为空");
        Assert.notNull(timeUnit, "timeUnit 不能为空");
        Date result;
        int timeIntValue;

        if (time > Integer.MAX_VALUE) {
            timeIntValue = Integer.MAX_VALUE;

        } else
            timeIntValue = Long.valueOf(time).intValue();
        //计算有效期
        switch (timeUnit) {
            case DAYS:
                result = org.apache.commons.lang3.time.DateUtils.addDays(date, timeIntValue);
                break;
            case HOURS:
                result = org.apache.commons.lang3.time.DateUtils.addHours(date, timeIntValue);
                break;
            case MINUTES:
                result = org.apache.commons.lang3.time.DateUtils.addMinutes(date, timeIntValue);
                break;
            case SECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addSeconds(date, timeIntValue);
                break;

            case MILLISECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, timeIntValue);
                break;
            default:
                result = date;
        }
        return result;
    }

    public static void main(String[] args) {
        Date now = now();
        System.out.println(now);
    }
}
