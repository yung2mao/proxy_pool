package cn.whitetown.config;

import java.util.concurrent.TimeUnit;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
public class ProxyConstants {
    public static final String CACHE_KEY = "proxy_free";
    public static final long CACHE_TIME = 24;
    public static final TimeUnit CACHE_UNIT = TimeUnit.HOURS;

    public static final int BASIC_SCORE = 10;
    public static final int ENABLE_SCORE = 13;
    public static final double MIN_SCORE = 0;
    public static final int CACHE_FETCH_SIZE = 200;
}
