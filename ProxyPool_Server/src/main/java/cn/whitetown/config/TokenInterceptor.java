package cn.whitetown.config;

import cn.whitetown.modo.ClientAuthProperties;
import cn.whitetown.web.base.exception.define.DefaultResException;
import cn.whitetown.web.base.interceptor.WhHandlerInterceptor;
import cn.whitetown.web.base.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: taixian
 * @date: created in 2021/01/04
 */
@Component
public class TokenInterceptor implements WhHandlerInterceptor {

    @Autowired
    private ClientAuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            resError(response);
            return false;
        }
        boolean contains = authProperties.getTokens().contains(token);
        if(!contains) {
            resError(response);
            return false;
        }
        return true;
    }

    private void resError(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(ResponseData.fail("401","not allowed").toString());
        writer.flush();
        writer.close();
    }
}
