package cn.whitetown.collector.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.whitetown.check.ProxyCheck;
import cn.whitetown.collector.ProxyFetch;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.util.CustomHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Component
public class SixSixProxyFetch implements ProxyFetch {

    @Autowired
    private ProxyCheck proxyCheck;

    @Value("${proxy.from.url66}")
    private String url;

    @Override
    public OwnProxy fetchProxy() throws Exception {
        Set<OwnProxy> proxies = fetchProxies();
        if(proxies.size() == 0) { return null; }
        int index = RandomUtil.randomInt(proxies.size());
        index = index < proxies.size() ? index : index - 1;
        return new ArrayList<>(proxies).get(index);
    }

    @Override
    public Set<OwnProxy> fetchProxies() {
        HttpRequest request = CustomHttpUtil.createGet(url);
        HttpResponse response = request.execute();
        if(response.getStatus() != HttpStatus.OK.value()) {
            return new HashSet<>();
        }
        List<OwnProxy> proxies = new ArrayList<>();
        String body = response.body();
        String htmlRegex = "<[a-zA-Z \"']+/>";
        body = body.replaceAll(htmlRegex,"");
        String lineSep = System.getProperty("line.separator");
        for(String proxy : body.split(lineSep)) {
            proxy = proxy.trim();
            String[] hostPort = proxy.split(":");
            if(hostPort.length != 2) {
                continue;
            }
            if(!Validator.isIpv4(hostPort[0]) || !NumberUtil.isNumber(hostPort[1])) {
                continue;
            }
            proxies.add(new OwnProxy(hostPort[0], Integer.parseInt(hostPort[1])));
        }
        return proxyCheck.batchProxyCheck(proxies, null);
    }

}
