package run.halo.app.model.support;

import java.io.File;

/**
 * <pre>
 *     公共常量
 * </pre>
 *
 * @author ryanwang
 * @date : 2017/12/29
 */
public class HaloConst {

    /**
     *
     * 用户的主目录
     */
    public final static String USER_HOME = System.getProperties().getProperty("user.home");

    /**
     *
     * 默认的主题的名称。
     */
    public final static String DEFAULT_THEME_ID = "caicai_anatole";

    /**
     * 版本控制
     */
    public static final String HALO_VERSION;

    /**
     * 路径分隔符。
     */
    public static final String FILE_SEPARATOR = File.separator;

    /**
     * Sfreemarker模板文件的后缀
     */
    public static final String SUFFIX_FTL = ".ftl";

    /**
     * 自定义freemarker标记方法键
     */
    public static final String METHOD_KEY = "method";
    /**
     * 网易云音乐短代码前缀
     */
    public static final String NETEASE_MUSIC_PREFIX = "[music:";
    /**
     * 网易云音乐 iframe 代码
     */
    public static final String NETEASE_MUSIC_IFRAME = "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 height=86 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=1&height=66\"></iframe>";
    /**
     * 网易云音乐短代码正则表达式
     */
    public static final String NETEASE_MUSIC_REG_PATTERN = "\\[music:(\\d+)\\]";
    /**
     * 哔哩哔哩视频短代码前缀
     */
    public static final String BILIBILI_VIDEO_PREFIX = "[bilibili:";
    /**
     * 哔哩哔哩视频 iframe 代码
     */
    public static final String BILIBILI_VIDEO_IFRAME = "<iframe height=$3 width=$2 src=\"//player.bilibili.com/player.html?aid=$1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"> </iframe>";
    /**
     * 哔哩哔哩视频正则表达式
     */
    public static final String BILIBILI_VIDEO_REG_PATTERN = "\\[bilibili:(\\d+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * YouTube 视频短代码前缀
     */
    public static final String YOUTUBE_VIDEO_PREFIX = "[youtube:";
    /**
     * YouTube 视频 iframe 代码
     */
    public static final String YOUTUBE_VIDEO_IFRAME = "<iframe width=$2 height=$3 src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
    /**
     * YouTube 视频正则表达式
     */
    public static final String YOUTUBE_VIDEO_REG_PATTERN = "\\[youtube:(\\w+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * user_session
     */
    public static String USER_SESSION_KEY = "user_session";

    static {
        // Set version
        HALO_VERSION = HaloConst.class.getPackage().getImplementationVersion();
    }
}
