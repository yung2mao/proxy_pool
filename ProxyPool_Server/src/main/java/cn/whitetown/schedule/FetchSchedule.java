package cn.whitetown.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.whitetown.collector.ProxyFetch;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.modo.ProxySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;

/**
 * 抓取代理定时任务
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Configuration
public class FetchSchedule {

    private Logger logger = LoggerFactory.getLogger(FetchSchedule.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ProxySet proxySet;

    private Set<ProxyFetch> fetchList = null;

    @Scheduled(initialDelay = 1000, fixedDelay = 100000)
    public void fetchTask() throws Exception {
        logger.info("fetch proxy task is started, current Time is {}", DateUtil.now());
        if(fetchList == null) {
            Collection<ProxyFetch> fetches = context.getBeansOfType(ProxyFetch.class).values();
            fetchList = new HashSet<>(fetches);
        }
        for(ProxyFetch proxyFetch : fetchList) {
            Set<OwnProxy> ownProxies = proxyFetch.fetchProxies();
            if(ownProxies.size() == 0) {
                continue;
            }
            proxySet.addAll(ownProxies);
        }
        logger.info("fetch proxy task is ended, total proxy size is {}, current time is {}", proxySet.size(), DateTime.now());
    }
}
