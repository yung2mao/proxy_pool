package cn.whitetown.check;

import cn.whitetown.modo.OwnProxy;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * 批量通用性校验
     * @param proxies 代理集
     * @param basicUrl 目标地址 为空时校验通用地址
     * @return 可用代理集
     */
    Set<OwnProxy> batchProxyCheck(Collection<OwnProxy> proxies, String basicUrl);
}
