package cn.whitetown.collector;

import cn.whitetown.modo.OwnProxy;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

/**
 * 代理抓取
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface ProxyFetch {

    /**
     * 抓取一个代理
     * @return -
     * @throws BrokenBarrierException -
     * @throws InterruptedException -
     */
    OwnProxy fetchProxy() throws BrokenBarrierException, InterruptedException;

    /**
     * 一次抓取多个代理
     * @return -
     * @throws BrokenBarrierException -
     * @throws InterruptedException -
     */
    List<OwnProxy> fetchProxies() throws BrokenBarrierException, InterruptedException;
}
