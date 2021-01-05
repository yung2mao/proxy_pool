package cn.whitetown.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import cn.whitetown.check.ProxyCheck;
import cn.whitetown.collector.CacheProxyFetch;
import cn.whitetown.config.ProxyConstants;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Service
public class ProxyServiceImpl implements ProxyService {

    @Autowired
    private CacheProxyFetch cacheProxyFetch;

    @Autowired
    private ProxyCheck proxyCheck;

    @Override
    public OwnProxy getEnableProxy() {
        OwnProxy proxy = cacheProxyFetch.randomEnableProxy(ProxyConstants.ENABLE_SCORE);
        if(proxy == null) {
            return cacheProxyFetch.randomEnableProxy(ProxyConstants.BASIC_SCORE);
        }
        return proxy;
    }

    @Override
    public List<OwnProxy> getEnableProxyList(Integer size) {
        List<OwnProxy> proxies = cacheProxyFetch.enableProxyList(ProxyConstants.ENABLE_SCORE);
        if(proxies.size() == 0) {
            proxies = cacheProxyFetch.enableProxyList(ProxyConstants.BASIC_SCORE);
        }
        Set<OwnProxy> results = new HashSet<>();
        for (int i = 0; i < size; i++) {
            if(proxies.size() == 0) {
                break;
            }
            int random = RandomUtil.randomInt(proxies.size());
            OwnProxy proxy = proxies.get(random);
            results.add(proxy);

        }
        return new ArrayList<>(results);
    }

    @Override
    public OwnProxy getUrlProxy(String basicUrl) {
        List<OwnProxy> proxyList = getEnableProxyList(10);
        Set<OwnProxy> proxySet = proxyCheck.batchProxyCheck(proxyList, basicUrl);
        if(proxySet.size() == 0) {
            return null;
        }
        int random = RandomUtil.randomInt(proxySet.size());
        return proxyList.get(random);
    }
}
