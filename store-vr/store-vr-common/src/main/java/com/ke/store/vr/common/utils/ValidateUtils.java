package com.ke.store.vr.common.utils;

import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 校验工具类
 */
public class ValidateUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验对象，并返回校验结果。
     * pair left 为检测字段;pair right 为错误信息。
     *
     * @param t
     * @return
     */
    public static List<Pair<String, String>> getValidateResult(Object t) {
        return validator.validate(t)
                .stream()
                .map(constraintViolation -> Pair.of(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
                .collect(Collectors.toList());
    }

    /**
     * 校验数组工具类
     * @param list
     * @return
     */
    public static Map<String, List<Pair<String, String>>> validateList(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        Map<String, List<Pair<String, String>>> validateResult = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            List<Pair<String, String>> result = getValidateResult(list.get(i));
            if (!CollectionUtils.isEmpty(result)) {
                validateResult.put(String.format("数组第%s个元素校验不通过", i), result);
            }
        }
        return validateResult;
    }
}
