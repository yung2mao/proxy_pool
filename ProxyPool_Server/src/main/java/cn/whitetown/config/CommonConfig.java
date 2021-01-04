package cn.whitetown.config;

import cn.whitetown.modo.ProxySet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用初始化方法
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Configuration
public class CommonConfig {

    @Bean
    public ProxySet proxyMap() {
        return new ProxySet();
    }
}
