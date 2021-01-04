package cn.whitetown.check.impl;

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
        HttpResponse response = request.execute();
        return response.getStatus() == HttpStatus.OK.value();
    }
}
