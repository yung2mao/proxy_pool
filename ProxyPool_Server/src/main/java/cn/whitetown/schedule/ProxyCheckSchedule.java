package cn.whitetown.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.check.ProxyCheck;
import cn.whitetown.config.ProxyConstants;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.modo.ProxySet;
import cn.whitetown.web.redis.manager.RedisSortedSetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Configuration
public class ProxyCheckSchedule {

    private Logger logger = LoggerFactory.getLogger(ProxyCheckSchedule.class);

    @Autowired
    private RedisSortedSetManager redisManager;

    @Autowired
    private ProxyCheck proxyCheck;

    @Autowired
    private ProxySet proxies;

    @Scheduled(initialDelay = 5000, fixedDelay = 100000)
    public void checkProxyTask() throws InterruptedException {
        logger.info("the proxy check task is started, the current time is {}", DateUtil.now());
        Map<String, Double> activeProxyMap = new HashMap<>(16);
        Set<OwnProxy> removeProxies = new HashSet<>();
        CountDownLatch countDownLatch = new CountDownLatch(proxies.size() + 1);
        //common check
        for(OwnProxy proxy : proxies) {
            ThreadUtil.execAsync(() -> {
                if(checkProxy(proxy)) {
                    activeProxyMap.put(proxy.getProxy(), (double) proxy.getScore());
                }else {
                    removeProxies.add(proxy);
                }
                countDownLatch.countDown();
            });
        }
        if(proxies.size() != 0) {
            countDownLatch.await(5, TimeUnit.SECONDS);
        }

        if(activeProxyMap.size() == 0) {
            return;
        }
        redisManager.save(ProxyConstants.CACHE_KEY, activeProxyMap, ProxyConstants.CACHE_TIME, ProxyConstants.CACHE_UNIT);
        redisManager.removeByScore(ProxyConstants.CACHE_KEY, Integer.MIN_VALUE, 1);
        proxies.removeAll(removeProxies);
        logger.info("the proxy check task is ended, total proxy size is {}, the current time is {}", activeProxyMap.size(), DateUtil.now());
    }

    private boolean checkProxy(OwnProxy proxy) {
        boolean pass = proxyCheck.commonCheck(proxy);
        if(!pass) {
            int score = proxy.getScore() > ProxyConstants.BASIC_SCORE ? ProxyConstants.BASIC_SCORE : proxy.getScore() - 1;
            proxy.setScore(score);
        }else if(proxy.getScore() < ProxyConstants.BASIC_SCORE) {
            proxy.setScore(ProxyConstants.BASIC_SCORE);
        }else {
            proxy.setScore(proxy.getScore() + 1);
        }
        return proxy.getScore() > 0;
    }
}
