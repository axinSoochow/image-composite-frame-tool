package com.axin.idea.validate.support;

import com.axin.idea.validate.domain.FieldCheckUnit;
import com.axin.idea.validate.domain.FieldExist;
import com.axin.idea.validate.domain.FieldType;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author axin
 * @since 2020-03-09
 * @summary 存在性校验支持类
 */
public class ValidateExistSupport {
    /**
     * 城市 存在性校验
     * @param field
     * @param value
     * @param unit
     * @param checkResult
     */
    public static void checkCityExist(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (unit.getExt() == null) {
            return;
        }

        try {
            if (unit.getExt().equals(FieldExist.城市.getCode()) && !ValidateSupport.cityCheck(value)) {
                checkResult.merge(field, FieldExist.城市.getErrMsg().apply(unit.getFieldName(), value), (oldV, newV) -> oldV + "," + newV);
                return;
            }

        } catch (Exception e) {
            checkResult.merge(field, "存在性校验:XX查询服务不可用", (oldV, newV) -> oldV + "," + newV);
        }
    }

    /**
     * 枚举存在性校验
     * @param field
     * @param value
     * @param unit
     * @param checkResult
     */
    public static void checkEnumCodeExist(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (CollectionUtils.isEmpty(unit.getEnumList()) || !unit.getFieldType().equals(FieldType.String.getType())) {
            return;
        }

        List<String> enumList = unit.getEnumList();
        if (!enumList.contains(value)) {
            String err = String.format("[%s]是一个无效的code", unit.getFieldName());
            checkResult.merge(field, err, (oldV, newV) -> oldV + "," + newV);
        }
    }
}
