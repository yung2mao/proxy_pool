package cn.whitetown.service;

import cn.whitetown.modo.OwnProxy;

/**
 * 代理服务
 * @author: taixian
 * @date: created in 2021/01/04
 */
public interface ProxyService {

    /**
     * 获取一个可用的通用代理
     * @return
     */
    OwnProxy getEnableProxy();
}
