package com.dhuer.mallchat.common.common.intercepetor;

import cn.hutool.extra.servlet.ServletUtil;
import com.dhuer.mallchat.common.common.domain.dto.RequestInfo;
import com.dhuer.mallchat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Description: 将请求的特征，ip，uid 都收集进 RequestHolder 中，并在 controller 执行完成后移除，方便在业务中轻松获取 uid
 * Author: Jintao Li
 * Date: 2024/4/28
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUid(uid);
        requestInfo.setIp(ServletUtil.getClientIP(request));
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
