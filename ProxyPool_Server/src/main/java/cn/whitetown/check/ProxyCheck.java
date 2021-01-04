package cn.whitetown.check;

import cn.whitetown.modo.OwnProxy;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface ProxyCheck {

    /**
     * 校验代理通用的可用性
     * @param proxy 代理
     * @return -
     */
    boolean commonCheck(OwnProxy proxy);

    /**
     * 校验代理针对指定url的可用性
     * @param proxy 代理
     * @param url 指定的url
     * @return -
     */
    boolean urlProxyCheck(OwnProxy proxy, String url);
}
