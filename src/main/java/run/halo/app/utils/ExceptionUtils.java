package run.halo.app.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具包
 */
public class ExceptionUtils {
    /**
     * <p>从一个可抛出的字符串中获取堆盏跟踪</p>
     * <p>此方法的结果因JDK版本不同而异
     * 使用{@link Throwable#printStackTrace(java.io.PrintWriter)}
     * 在JDK1.3和更早版本中，不会显示原因异常
     * 除非指定的可丢弃项更改printstackTrace</p>
     * @param throwable 这 <code> Throwable</code> 要检查
     * @return 由异常生成的堆栈跟踪
     * <code>printStackTrace(PrintWriter)</code> method
     */
    public static String getStackTrace(final Throwable throwable){
        final StringWriter sw=new StringWriter();

        final PrintWriter pw=new PrintWriter(sw,true);

        throwable.printStackTrace(pw);

        return sw.getBuffer().toString();
    }


}
