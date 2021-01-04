package cn.whitetown.modo;

import cn.whitetown.config.ProxyConstants;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Getter
@Setter
public class OwnProxy {
    private String ip;
    private Integer port;
    private int score;
    private Set<String> includeUrls = new HashSet<>();
    private Set<String> excludeUrls = new HashSet<>();
    private String content;

    public String getProxy() {
        return ip + ":" + port;
    }

    public OwnProxy(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        this.score = ProxyConstants.BASIC_SCORE;
    }

    public OwnProxy(String ip, Integer port, String content) {
        this(ip, port);
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        OwnProxy ownProxy = (OwnProxy) o;
        return Objects.equals(ip, ownProxy.ip) &&
                Objects.equals(port, ownProxy.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
