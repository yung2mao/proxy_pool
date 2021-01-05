package cn.whitetown.collector;

import cn.whitetown.modo.OwnProxy;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeoutException;

/**
 * 代理抓取
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface ProxyFetch {

    /**
     * 抓取一个代理
     * @return -
     * @throws Exception e
     */
    OwnProxy fetchProxy() throws Exception;

    /**
     * 一次抓取多个代理
     * @return -
     * @throws Exception e
     */
    Set<OwnProxy> fetchProxies() throws Exception;
}
