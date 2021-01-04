package cn.whitetown.schedule;

import cn.hutool.core.date.DateUtil;
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
import java.util.Map;

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
    public void checkProxy() {
        logger.info("the proxy check task is started, the current time is {}", DateUtil.now());
        Map<OwnProxy, Double> activeProxyMap = new HashMap<>(16);
        for(OwnProxy proxy : proxies) {
            if(checkProxy(proxy)) {
                activeProxyMap.put(proxy, (double) proxy.getScore());
            }
        }
        if(activeProxyMap.size() == 0) {
            return;
        }
        redisManager.save(ProxyConstants.CACHE_KEY, activeProxyMap, ProxyConstants.CACHE_TIME, ProxyConstants.CACHE_UNIT);
        logger.info("the proxy check task is ended, total proxy size is {}, the current time is {}", activeProxyMap.size(), DateUtil.now());
    }

    private boolean checkProxy(OwnProxy proxy) {
        boolean pass = proxyCheck.commonCheck(proxy);
        if(!pass) {
            proxy.setScore(proxy.getScore() - 1);
        }else if(proxy.getScore() < ProxyConstants.BASIC_SCORE) {
            proxy.setScore(ProxyConstants.BASIC_SCORE);
        }else {
            proxy.setScore(proxy.getScore() + 1);
        }
        return proxy.getScore() > 0;
    }
}
