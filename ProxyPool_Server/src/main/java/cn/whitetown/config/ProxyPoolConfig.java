package cn.whitetown.config;

import cn.whitetown.modo.ProxySet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 通用初始化方法
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Configuration
public class ProxyPoolConfig {

    @Value("${thread.pool.capacity}")
    private Integer poolCapacity;

    @Bean
    public ProxySet proxyMap() {
        return new ProxySet();
    }

    /**
     * 获取代理池的容量
     * @return
     */
    public int poolCapacity() {
        return poolCapacity == null ? Byte.MAX_VALUE : poolCapacity;
    }
}
