package cn.whitetown.collector.impl;

import cn.hutool.core.util.RandomUtil;
import cn.whitetown.collector.CacheProxyFetch;
import cn.whitetown.config.ProxyConstants;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.util.ProxyCacheManager;
import cn.whitetown.web.redis.manager.RedisSortedSetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: taixian
 * @date: created in 2021/01/05
 */
@Component
public class CacheProxyFetchImpl implements CacheProxyFetch {

    @Autowired
    private ProxyCacheManager proxyCacheManager;

    @Override
    public OwnProxy randomEnableProxy(Integer minScore) {
        List<OwnProxy> proxies = enableProxyList(minScore);
        if(proxies.size() == 0) {
            return null;
        }
        int index = RandomUtil.randomInt(proxies.size());
        return proxies.get(index);
    }

    @Override
    public List<OwnProxy> enableProxyList(Integer minScore) {
        List<OwnProxy> proxies = proxyCacheManager.topNumber(ProxyConstants.CACHE_KEY, ProxyConstants.CACHE_FETCH_SIZE);
        if(proxies == null) {
            return new ArrayList<>();
        }
        return proxies.stream()
                .filter(ele -> ele.getScore() >= minScore)
                .distinct()
                .collect(Collectors.toList());
    }
}
