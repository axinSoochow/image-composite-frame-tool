package com.axin.idea.validate;

import com.axin.idea.validate.domain.FieldCheckUnit;
import com.axin.idea.validate.domain.FieldType;
import com.axin.idea.validate.domain.ValidateConfig;
import com.axin.idea.validate.domain.ValidateConfigException;
import com.axin.idea.validate.support.ValidateExistSupport;
import com.axin.idea.validate.support.ValidateListSupport;
import com.axin.idea.validate.support.ValidateSupport;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author axin
 * @summary 校验引擎
 */
public abstract class AbstractValidate {

    /**
     * 校验核心方法
     * @param params
     * @param key
     * @return 校验结果
     */
    public final Map<String, String> check(Map<String, Object> params, String key) {
        if (params == null) {
            throw new ValidateConfigException("只有Map结构或继承AxinValidate<T>类的对象参数才可以进行校验");
        }
        checkBefore(params, key);

        //获得校验配置
        ValidateConfig validateConfig = getValidateConfig(params, key);
        if (validateConfig == null || validateConfig.getFields() == null) {
            throw new ValidateConfigException("找不到接口数据对应的校验配置，请检查getValidateConfig()方法逻辑是否正确");
        }
        Map<String, String> checkResult = new LinkedHashMap<>();
        //遍历配置检查参数
        for (FieldCheckUnit unit : validateConfig.getFields()) {
            Object value = params;

            //获得嵌套中的变量值value
            String[] nestArr = unit.getFieldCode().split("\\.");
            for (String field : nestArr) {
                if (!StringUtils.isEmpty(field) && value instanceof Map) {
                    Map<String, Object> paramsTemp = (Map) value;
                    value = paramsTemp.get(field);
                }
            }

            if (unit.getFieldType().contains(FieldType.List.getType()) || value instanceof List) {
                ValidateListSupport.listCheck(unit.getFieldCode(), value, unit, validateConfig, checkResult);
                continue;
            }

            this.checkUnit(unit.getFieldCode(), value, unit, checkResult);
        }

        checkAfter(params, key, validateConfig, checkResult);
        return checkResult;
    }

    /**
     * 获得配置
     * @param params
     * @param key @ValidateCheck注解 key的值
     * @return
     */
    protected abstract ValidateConfig getValidateConfig(Map<String, Object> params, String key);

    /**
     * 校验之前 - 可用于项目中自定义校验（例如技术字段校验、幂等校验等）
     * @param key @ValidateCheck注解 key的值
     * @param params 接口参数
     */
    protected abstract void checkBefore(Map<String, Object> params, String key);

    /**
     * 校验后 - 扩展方法 - 可用于处理校验的结果
     *
     * @param checkResult 检验结果
     * @param validateConfig 参数对应的校验配置
     * @param key         @ValidateCheck注解 key的值
     * @param params      接口参数
     */
    protected abstract void checkAfter(Map<String, Object> params, String key, ValidateConfig validateConfig, Map<String, String> checkResult);

    /**
     * 存在性校验 如不想使用 则子类可覆盖该方法
     *
     * @param field       变量在配置中的定义 fieldCode
     * @param value       数据中对应的值
     * @param unit        校验单元
     * @param checkResult
     */
    protected void checkExist(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
//
        ValidateExistSupport.checkCityExist(field, value, unit, checkResult);
        ValidateExistSupport.checkEnumCodeExist(field, value, unit, checkResult);
//        ValidateExistSupport.checkUCExist(field, value, unit, checkResult, xxSPI);
//        ValidateExistSupport.checkOrgExist(field, value, unit, checkResult, xxSPI);
    }

    /**
     * 基础合法性校验
     * @param field 变量名
     * @param value 变量值
     * @param unit 校验单元
     * @param checkResult
     */
    private void checkUnit(String field, Object value, FieldCheckUnit unit, Map<String, String> checkResult) {
        //从参数中获得变量名进行检查
        if (value instanceof Map) {
            String err = String.format("嵌套类型变量[%s]的fieldCode配置错误!", field);
            throw new ValidateConfigException(err);
        }

        ValidateSupport.configCheck(field, unit);
        //基础校验
        ValidateSupport.fieldRequireCheck(field, value, unit, checkResult);
        if (value == null) {return;}
        String stringValue = ValidateSupport.getStringValue(value);

        ValidateSupport.fieldTypeCheck(field, stringValue, unit, checkResult);
        ValidateSupport.fieldStrCheck(field, stringValue, unit, checkResult);
        ValidateSupport.fieldNumberCheck(field, stringValue, unit, checkResult);

        //扩展校验-存在性
        this.checkExist(field, stringValue, unit, checkResult);
    }


}
