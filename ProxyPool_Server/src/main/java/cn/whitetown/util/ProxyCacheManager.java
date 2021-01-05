package cn.whitetown.util;

import cn.whitetown.modo.OwnProxy;
import cn.whitetown.web.redis.manager.RedisSortedSetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: taixian
 * @date: created in 2021/01/05
 */
@Component
public class ProxyCacheManager{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public List<OwnProxy> get(String key, double minScore) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.boundZSetOps(key).reverseRangeByScoreWithScores(minScore, Double.MAX_VALUE);
        return dataChange(typedTuples);
    }

    public List<OwnProxy> topNumber(String key, long size) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.boundZSetOps(key).reverseRangeWithScores(0, size - 1);
        return dataChange(typedTuples);
    }

    private List<OwnProxy> dataChange(Set<ZSetOperations.TypedTuple<String>> typedTuples) {
        if(typedTuples == null) {
            return new ArrayList<>();
        }
        return typedTuples.stream()
                .map(ele -> {
                    String[] hostPort = ele.getValue().split(":");
                    if(hostPort.length != 2) {
                        return null;
                    }
                    OwnProxy proxy = new OwnProxy(hostPort[0], Integer.parseInt(hostPort[1]));
                    proxy.setScore(ele.getScore().intValue());
                    return proxy;
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
