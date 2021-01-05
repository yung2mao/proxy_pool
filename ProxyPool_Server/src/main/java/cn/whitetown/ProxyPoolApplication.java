package cn.whitetown;

import cn.whitetown.config.ProxyConstants;
import cn.whitetown.modo.OwnProxy;
import cn.whitetown.modo.ProxySet;
import cn.whitetown.util.ProxyCacheManager;
import cn.whitetown.web.redis.manager.RedisSortedSetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * application
 * @author taixian
 */
@SpringBootApplication
public class ProxyPoolApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(ProxyPoolApplication.class);

    @Autowired
    private ProxySet proxies;

    @Autowired
    private ProxyCacheManager proxyCacheManager;

    public static void main( String[] args ) {
        long begin = System.currentTimeMillis();
        SpringApplication.run(ProxyPoolApplication.class, args);
        long totalTime = System.currentTimeMillis() - begin;
        logger.warn("the proxy pool server is started, total time is {}", totalTime);
    }

    @Override
    public void run(String... args) {
        List<OwnProxy> proxies = proxyCacheManager.get(ProxyConstants.CACHE_KEY, ProxyConstants.MIN_SCORE);
        this.proxies.addAll(proxies);
        logger.info("init cache proxies success, total size is {}", proxies.size());
    }
}
