package cn.whitetown.collector;

import cn.whitetown.modo.OwnProxy;

import java.util.List;

/**
 * 本地缓存的代理获取
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface CacheProxyFetch {

    /**
     * 获取基础评分大于一定数值的proxy
     * @param minScore 最小评分
     * @return -
     */
    OwnProxy randomEnableProxy(Integer minScore);

    /**
     * 获取基础评分大于一定数值的proxy集合
     * @param minScore 最小评分
     * @return -
     */
    List<OwnProxy> enableProxyList(Integer minScore);
}
