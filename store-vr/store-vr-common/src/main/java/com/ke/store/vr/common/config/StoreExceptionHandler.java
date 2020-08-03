package com.ke.store.vr.common.config;

import com.ke.store.vr.common.model.error.StoreApiException;
import com.ke.store.vr.common.model.error.StoreBusinessException;
import com.ke.store.vr.common.model.error.StoreCommonErrCode;
import com.ke.store.vr.common.model.error.StoreInnerException;
import com.ke.store.vr.domain.ApiResult;
import com.ke.store.vr.domain.StoreError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author axin
 * @summary controller 统一异常处理器
 */
@Order(1)
@Slf4j
@Configuration
@RestControllerAdvice
public class StoreExceptionHandler {

    public StoreExceptionHandler() {
        log.info("===========================================");
        log.info("=                                         =");
        log.info("=          Store Error Handler Start        =");
        log.info("=                                         =");
        log.info("===========================================");
    }

    /**
     * 系统外异常处理
     * @param sm
     * @return
     */
    @ResponseBody
    @ExceptionHandler(StoreApiException.class)
    public ApiResult exceptionHandlerAPI(StoreApiException sm) {
        sm.getErrCode().getLevel().log(log, sm);
        log.info("异常参数信息:{}", sm.getErrCode());
        return ApiResult.response(sm.getErrCode());
    }

    /**
     * 系统内异常处理
     * @param sm
     * @return
     */
    @ResponseBody
    @ExceptionHandler(StoreBusinessException.class)
    public ApiResult exceptionHandlerBusiness(StoreBusinessException sm) {
        String URL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletPath();
        sm.getErrCode().getLevel().log(log, sm);
        log.info("异常参数信息:{}", sm.getErrCode());
        return ApiResult.response(sm.getErrCode(),URL);
    }

    /**
     * 预期外的内部异常
     * @param sm
     * @return
     */
    @ResponseBody
    @ExceptionHandler(StoreInnerException.class)
    public ApiResult exceptionHandlerInner(StoreInnerException sm) {
        sm.getErrCode().getLevel().log(log, sm);
        log.info("异常参数信息:{}", sm.getErrCode());
        return ApiResult.response(StoreError.未知.withMsg("系统出现预期外的异常"));
    }

    /**
     * 参数校验异常处理 ConstraintViolationException 处理 @Validated抛出的错误
     * @param c
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult exceptionHandlerValidate(ConstraintViolationException c) {
        Set<ConstraintViolation<?>> violations = c.getConstraintViolations();
        List<String> message = violations.stream().map(v -> v.getMessage()).collect(Collectors.toList());
        log.error("接口有不符合规范的请求,详情:{}，{}", message, c.getMessage());
        return ApiResult.response(StoreCommonErrCode.请求参数错误.withMsg("不符合接口约定!"), message);
    }

    /**
     * 参数校验异常处理 MethodArgumentNotValidException 处理 @Validated抛出的错误
     *
     * @param c
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult exceptionHandlerValidateMethod(MethodArgumentNotValidException m) {
        List<ObjectError> allErrors = m.getBindingResult().getAllErrors();
        List<String> message = allErrors.stream().map(v -> v.getDefaultMessage()).collect(Collectors.toList());
        log.error("接口有不符合规范的请求,详情:{}，{}", message, m.getMessage());
        return ApiResult.response(StoreCommonErrCode.请求参数错误.withMsg("不符合接口约定!"), message);
    }

    //———————————————————————— Spring MVC 接口相关异常 ————————————————————————————

    /**
     * 参数校验异常处理 处理 SpringMVC 参数绑定 抛出的错误
     *
     * @param c
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiResult exceptionHandlerValidateMethod(ServletRequestBindingException m) {
        log.error("接口有不符合规范的请求,详情:{}", m.getMessage());
        return ApiResult.response(StoreCommonErrCode.请求参数错误.withMsg("不符合接口约定!"), m.getMessage());
    }

    /**
     * 参数校验异常处理 处理 SpringMVC 类型转换 抛出的错误
     * @param c
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult exceptionHandlerValidateMethod(MethodArgumentTypeMismatchException m) {
        log.error("接口有不符合规范的请求,详情:{}", m.getMessage());
        return ApiResult.response(StoreCommonErrCode.请求参数错误.withMsg("不符合接口约定!"), m.getMessage());
    }

    /**
     * 服务器未知异常处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResult exceptionHandlerException(Exception ex) {
        log.error("未知异常", ex);
        return ApiResult.response(StoreError.未知);
    }

}
