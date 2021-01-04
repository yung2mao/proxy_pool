package cn.whitetown.controller;

import cn.whitetown.modo.OwnProxy;
import cn.whitetown.service.ProxyService;
import cn.whitetown.web.base.exception.define.DefaultResException;
import cn.whitetown.web.base.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
     * @param request request
     * @return -
     */
    @GetMapping
    public ResponseData<OwnProxy> proxy(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            throw new DefaultResException("401", "未授权");
        }
        OwnProxy proxy = proxyService.getEnableProxy();
        return ResponseData.ok(proxy);
    }

    /**
     * 获取一个指定url可用的代理
     * @param basicUrl
     * @param request
     * @return
     */
    @GetMapping("/urls")
    public ResponseData<OwnProxy> urlProxy(String basicUrl, HttpServletRequest request) {
        return null;
    }
}
