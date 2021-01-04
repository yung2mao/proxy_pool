package cn.whitetown.modo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Component
public class ClientAuthProperties {
    private Set<String> tokens;

    public Set<String> getTokens() {
        return tokens;
    }

    @Value("${proxy.client.tokens}")
    public void setTokens(String tokens) {
        String[] tokenArr = tokens.split(",");
        this.tokens = new HashSet<>();
        this.tokens.addAll(Arrays.asList(tokenArr));
    }
}
