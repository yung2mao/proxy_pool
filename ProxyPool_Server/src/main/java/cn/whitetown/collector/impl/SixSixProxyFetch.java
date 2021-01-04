package cn.whitetown.collector.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.thread.ThreadUtil;
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
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
    public OwnProxy fetchProxy() throws BrokenBarrierException, InterruptedException {
        List<OwnProxy> proxies = fetchProxies();
        if(proxies.size() == 0) { return null; }
        int index = RandomUtil.randomInt(proxies.size());
        index = index < proxies.size() ? index : index - 1;
        return proxies.get(index);
    }

    @Override
    public List<OwnProxy> fetchProxies() throws BrokenBarrierException, InterruptedException {
        HttpRequest request = CustomHttpUtil.createGet(url);
        HttpResponse response = request.execute();
        if(response.getStatus() != HttpStatus.OK.value()) {
            return new ArrayList<>();
        }
        List<OwnProxy> results = new ArrayList<>();
        String body = response.body();
        String htmlRegex = "<[a-zA-Z \"']+/>";
        body = body.replaceAll(htmlRegex,"");
        List<String[]> proxies = new ArrayList<>();
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
            proxies.add(hostPort);
        }
        CyclicBarrier cyclicBarrier = new CyclicBarrier(proxies.size());
        proxies.forEach(hostPort -> {
            OwnProxy ownProxy = new OwnProxy(hostPort[0], Integer.parseInt(hostPort[1]));
            ThreadUtil.execAsync(() -> {
                try {
                    checkAndPut(ownProxy, results, cyclicBarrier);
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        cyclicBarrier.await();
        return results;
    }

    private void checkAndPut(OwnProxy proxy, List<OwnProxy> proxyList, CyclicBarrier cyclicBarrier) throws BrokenBarrierException, InterruptedException {
        boolean pass = proxyCheck.commonCheck(proxy);
        if(pass) {
            synchronized (this) {
                proxyList.add(proxy);
            }
        }
        cyclicBarrier.await();
    }
}
