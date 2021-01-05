package cn.whitetown.check.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.whitetown.check.ProxyCheck;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.util.CustomHttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Component
public class DefaultProxyCheck implements ProxyCheck {

    @Value("${proxy.target.commonUrl}")
    private String commonUrl;

    @Override
    public boolean commonCheck(OwnProxy proxy) {
        HttpRequest request = CustomHttpUtil.createGet(commonUrl);
        request.setProxy(CustomHttpUtil.createProxy(proxy.getIp(), proxy.getPort()));
        HttpResponse response;
        try {
            response = request.execute();
        }catch (Exception e) {
            return false;
        }
        if(response.getStatus() != HttpStatus.OK.value()) {
            return false;
        }
        JSONObject resData = JSON.parseObject(response.body());
        return resData.getString("origin").equals(proxy.getIp());
    }

    @Override
    public boolean urlProxyCheck(OwnProxy proxy, String url) {
        HttpRequest request = CustomHttpUtil.createGet(url);
        request.setProxy(CustomHttpUtil.createProxy(proxy.getIp(), proxy.getPort()));
        HttpResponse response;
        try {
            response = request.execute();
        }catch (Exception e) {
            return false;
        }
        return response.getStatus() == HttpStatus.OK.value();
    }

    @Override
    public Set<OwnProxy> batchProxyCheck(Collection<OwnProxy> proxies, String basicUrl) {
        Set<OwnProxy> proxySet = new LinkedHashSet<>();
        if(proxies.size() == 0) {
            return proxySet;
        }
        CountDownLatch countDownLatch = new CountDownLatch(proxies.size() + 1);
        for(OwnProxy proxy : proxies) {
            ThreadUtil.execAsync(() -> {
                boolean pass = StringUtils.isEmpty(basicUrl) ? commonCheck(proxy) : urlProxyCheck(proxy, basicUrl);
                if(pass) {
                    proxySet.add(proxy);
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return proxySet;
    }
}
