package cn.whitetown.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.whitetown.modo.ReqHeader;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
public class CustomHttpUtil {

    public static HttpRequest createGet(String url) {
        HttpRequest request = HttpUtil.createGet(url);
        request.header(ReqHeader.USER_AGENT.getKey(), ReqHeader.USER_AGENT.getValue());
        request.timeout(2000);
        return request;
    }

    /**
     * 创建代理
     * @param host 主机地址
     * @param port 端口号
     * @return -
     */
    public static Proxy createProxy(String host, Integer port) {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }
}
