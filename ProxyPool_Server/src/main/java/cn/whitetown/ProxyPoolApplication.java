package cn.whitetown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * application
 * @author taixian
 */
@SpringBootApplication
public class ProxyPoolApplication {

    private static Logger logger = LoggerFactory.getLogger(ProxyPoolApplication.class);

    public static void main( String[] args ) {
        long begin = System.currentTimeMillis();
        SpringApplication.run(ProxyPoolApplication.class, args);
        long totalTime = System.currentTimeMillis() - begin;
        logger.warn("the proxy pool server is started, total time is {}", totalTime);
    }
}
