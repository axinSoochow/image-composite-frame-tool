package com.axin.idea.validate.aop;

import com.axin.idea.validate.AbstractValidate;
import com.axin.idea.validate.domain.AxinValidate;
import com.axin.idea.validate.domain.ValidateConfigException;
import com.axin.idea.validate.domain.ValidateSkipException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author Axin
 * @since 2020-02-17
 * 接口字段合法性校验AOP
 */
@Aspect
public class ValidateAop {

    private AbstractValidate abstractValidate;

    public ValidateAop(AbstractValidate abstractValidate) {
        this.abstractValidate = abstractValidate;
    }

    @Before("@annotation(com.axin.idea.validate.aop.ValidateCheck)&&(args(params))")
    public void validateBefore(JoinPoint joinPoint, Object params) {
        Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Logger logger = LoggerFactory.getLogger(proxyMethod.getDeclaringClass());

        String key;
        String name;
        try {
            Method targetMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
            ValidateCheck validateCheck = targetMethod.getAnnotation(ValidateCheck.class);
            key = validateCheck.key();
            name = validateCheck.name();
        } catch (Exception e) {
            logger.error("ValidateAop拦截方法出现异常！", e);
            return;
        }

        //参数转换为Map
        Map<String, Object> paramsMap = null;
        if (params instanceof Map) {
            paramsMap = (Map)params;
        }
        if (params instanceof AxinValidate) {
            AxinValidate axinValidateParam = (AxinValidate) params;
            paramsMap = axinValidateParam.getParamsMap(params);
        }

        Map<String, String> checkResult = null;
        try {
            checkResult = abstractValidate.check(paramsMap, key);
        }
        catch (ValidateConfigException e) {
            logger.error("无法校验key={}，name={}的接口参数！原因:{},参数详情为:{}", key, name, e.getMessage(), paramsMap);
            throw new RuntimeException("接口校验组件配置出现异常:" + e.getMessage(), e);
        }
        catch (ValidateSkipException e) {
            logger.warn("key={}，name={}的接口参数跳出检查！,参数详情为:{}", key, name);
            return;
        }

        //保存结果
        if (params instanceof AxinValidate && !CollectionUtils.isEmpty(checkResult)) {
            ((AxinValidate) params).setCheckErrResult(checkResult);
        }
        if (params instanceof Map && !CollectionUtils.isEmpty(checkResult)) {
            ((Map) params).put(AxinValidate.checkResultFieldCode, checkResult);
        }

    }
}
