package cn.whitetown.config;

import cn.whitetown.web.base.util.WhiteThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * @author: taixian
 * @date: created in 2021/01/06
 */
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolConfig {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    private Integer coreSize;
    private Integer maxSize;
    private Integer queueSize;

    @Bean
    public ExecutorService executorService() {
        String threadPoolName = "proxy_thread_";
        ExecutorService threadPool = WhiteThreadPoolFactory
                .executorService(coreSize, maxSize, 10, queueSize, threadPoolName);
        logger.info("created thread pool, name is {}",threadPoolName);
        return threadPool;
    }

    public Integer getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
        this.coreSize = coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }
}
