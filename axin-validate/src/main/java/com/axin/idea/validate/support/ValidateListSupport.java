package com.axin.idea.validate.support;

import com.axin.idea.validate.domain.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author axin
 * @summary 数组结构支持类
 */
public class ValidateListSupport {

    /**
     * 数组结构校验
     *
     * @param field          变量名
     * @param value          值
     * @param validateConfig 校验配置
     * @param checkResult    校验结果
     */
    public static void listCheck(String field, Object value, FieldCheckUnit unit, ValidateConfig validateConfig, Map<String, String> checkResult) {
        //获取数组List的泛型
        FieldCheckUnit listUnit = new FieldCheckUnit();
        BeanUtils.copyProperties(unit, listUnit);
        listUnit.setFieldType(listTypeProcess(unit.getFieldType()));
        ValidateSupport.configCheck(field, listUnit);

        boolean isList = value instanceof List;
        List<Map> listParams = (List<Map>) value;

        //数组类型检查
        if (!(isList && unit.getFieldType().contains(FieldType.List.getType()))) {
            if (listUnit.getRequire()) {
                String err = String.format("[%s]不是约定的数组类型", unit.getFieldName());
                checkResult.merge(field, err, (oldV, newV) -> oldV + "," + newV);
            }
            return;
        }

        //数组必填检查
        if (CollectionUtils.isEmpty(listParams)) {
            if (listUnit.getRequire()) {
                String err = String.format("[%s]是必填字段", listUnit.getFieldName());
                checkResult.put(field, err);
            }
            return;
        }

        //普通List检查
        if (!FieldType.Object.getType().equals(listUnit.getFieldType())) {
            int index = 0;
            for (Object param : listParams) {
                if (param == null) {
                    checkResult.merge(field, "数组中存在null元素", (oldV, newV) -> oldV + "," + newV);
                    return;
                }
                String stringValue = param.toString();
                if (StringUtils.isEmpty(value)) {
                    String err = String.format("数组中第%s个元素为null", index);
                    checkResult.put(field, err);
                }
                ValidateSupport.fieldTypeCheck(field, stringValue, listUnit, checkResult);
                ValidateSupport.fieldStrCheck(field, stringValue, listUnit, checkResult);
                ValidateSupport.fieldNumberCheck(field, stringValue, listUnit, checkResult);
                index++;
            }
            return;
        }

        //嵌套对象检查
        List<ListCheckUnit> listCheckUnits = validateConfig.getListFields();
        if (CollectionUtils.isEmpty(listCheckUnits)) {
            return;
        }

        for (ListCheckUnit listCheckUnit : listCheckUnits) {
            if (field.equals(listCheckUnit.getListKey())) {
                //对象检查
                listTypeCheck(field, listParams, listCheckUnit, checkResult);
            }
        }
    }

    /**
     * 数组对象检查
     * @param field
     * @param value
     * @param listCheckUnit
     * @param checkResult
     */
    private static void listTypeCheck(String field, List<Map> value, ListCheckUnit listCheckUnit, Map<String, String> checkResult) {
        List<FieldCheckUnit> fieldCheckUnits = listCheckUnit.getFields();
        if (CollectionUtils.isEmpty(fieldCheckUnits)) {
            String err = String.format("数组变量[%s]的配置错误!", field);
            throw new ValidateConfigException(err);
        }

        for (Integer i = 0; i < value.size(); i++) {
            for (FieldCheckUnit fieldCheckUnit : fieldCheckUnits) {

                Map fieldParam = value.get(i);
                if (CollectionUtils.isEmpty(fieldParam)) {
                    checkResult.merge(field, String.format("数组中第%s个元素为null", i), (oldV, newV) -> oldV + "," + newV);
                    break;
                }
                ValidateSupport.configCheck(field, fieldCheckUnit);

                Object objectValue = fieldParam.get(fieldCheckUnit.getFieldCode());
                if (fieldCheckUnit.getRequire() && StringUtils.isEmpty(objectValue)) {
                    String err = String.format("数组中第%s个元素的%s必填", i, fieldCheckUnit.getFieldName());
                    checkResult.merge(field, err, (oldV, newV) -> oldV + "," + newV);
                    continue;
                }
                if (Objects.isNull(objectValue)) {
                    continue;
                }

                String listKey = field.concat("_").concat(i.toString()).concat("_").concat(fieldCheckUnit.getFieldCode());

                String stringValue = ValidateSupport.getStringValue(objectValue);
                ValidateSupport.fieldTypeCheck(listKey, stringValue, fieldCheckUnit, checkResult);
                ValidateSupport.fieldStrCheck(listKey, stringValue, fieldCheckUnit, checkResult);
                ValidateSupport.fieldNumberCheck(listKey, stringValue, fieldCheckUnit, checkResult);
                //TODO:不支持数组格式中的存在性校验
            }
        }
    }

    /**
     * 获取数组类型的泛型
     * @return
     */
    private static String listTypeProcess(String fieldType) {
        if (!(fieldType.contains("<") && fieldType.contains(">"))) {
            return fieldType;
        }
        int strStartIndex = fieldType.indexOf("<");
        int strEndIndex = fieldType.indexOf(">");

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return fieldType;
        }
        if (strEndIndex < 0) {
            return fieldType;
        }

        /* 开始截取 */
        String result = fieldType.substring(strStartIndex, strEndIndex).substring("<".length());
        return result;
    }
}
