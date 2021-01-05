package cn.whitetown.service;

import cn.whitetown.modo.OwnProxy;

import java.util.List;

/**
 * 代理服务
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface ProxyService {

    /**
     * 获取一个可用的通用代理
     * @return -
     */
    OwnProxy getEnableProxy();

    /**
     * 获取一组可用的通用代理列表
     * @param size 数量
     * @return -
     */
    List<OwnProxy> getEnableProxyList(Integer size);

    /**
     * 获取一个针对指定url可用代理
     * @param basicUrl 指定url
     * @return -
     */
    OwnProxy getUrlProxy(String basicUrl);
}
