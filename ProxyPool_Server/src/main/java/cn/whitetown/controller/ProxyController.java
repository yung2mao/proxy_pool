package cn.whitetown.controller;

import cn.whitetown.modo.OwnProxy;
import cn.whitetown.service.ProxyService;
import cn.whitetown.web.base.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理池对外服务
 * @author: taixian
 * @date: created in 2021/01/04
 */
@RestController
@RequestMapping("/proxies")
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    /**
     * 获取一个通用代理
     * @return -
     */
    @GetMapping
    public ResponseData<OwnProxy> proxy() {
        OwnProxy proxy = proxyService.getEnableProxy();
        return ResponseData.ok(proxy);
    }

    /**
     * 获取多个代理
     * @param size 数量
     * @return -
     */
    @GetMapping("/list")
    public ResponseData<List<OwnProxy>> proxyList(@RequestParam("size") Integer size) {
        List<OwnProxy> proxies = proxyService.getEnableProxyList(size);
        return ResponseData.ok(proxies);
    }

    /**
     * 获取一个指定url可用的代理
     * @param basicUrl 需要访问的url
     * @return -
     */
    @GetMapping("/urls")
    public ResponseData<OwnProxy> urlProxy(@RequestParam("basicUrl") String basicUrl) {
        OwnProxy proxy = proxyService.getUrlProxy(basicUrl);
        return ResponseData.ok(proxy);
    }

    /**
     * 获取指定url可用代理
     * @param basicUrl 目标url
     * @param size 长度
     * @return -
     */
    @GetMapping("/urls/list")
    public ResponseData<List<OwnProxy>> urlProxyList(@RequestParam("basicUrl") String basicUrl,
                                                     @RequestParam("size") Integer size) {

        return null;
    }
}
