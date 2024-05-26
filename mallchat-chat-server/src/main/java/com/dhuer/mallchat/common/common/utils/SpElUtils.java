package com.dhuer.mallchat.common.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/26
 */
public class SpElUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 获取方法全限定名
     * @param method
     * @return
     */
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass()+"#"+method.getName();
    }

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});
        // el 解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i=0; i<params.length; i++) {
            // 所有参数作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        Expression expression = PARSER.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }
}
