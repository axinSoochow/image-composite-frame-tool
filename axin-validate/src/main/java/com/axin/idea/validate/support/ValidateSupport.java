package com.axin.idea.validate.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.axin.idea.validate.AxinValidateCacheProperties;
import com.axin.idea.validate.domain.*;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @summary 校验支持工具类
 */
@Slf4j
public class ValidateSupport {

    /**  进程缓存开关*/
    private static boolean enable = true;

    private static Cache<String, Object> localCache = null;

    //——————————————————校验组件小工具—————————————————————
    /**
     * 获取校验失败结果Map
     * {"field":"不通过校验的原因"}
     * 组件默认提供该种类型的校验失败结果结构
     * @param params
     * @return
     */
    public static Map<String, String> getCheckResult(Map<String,Object> params) {
        Map<String, String> mapOb = (Map) params.get(AxinValidate.checkResultFieldCode);
        params.remove(AxinValidate.checkResultFieldCode);
        return mapOb;
    }

    /**
     * 获取校验失败结果Map
     * 若需要此种类型的校验结果，可以在checkAfter扩展方法中调用convertNameCheckResult方法生成
     * {"field的中文名":"不通过校验的原因"}
     * @param params
     * @return
     */
    public static Map<String, String> getCheckResultName(Map<String,Object> params) {
        Map<String, String> mapOb = (Map) params.get(AxinValidate.checkResultFieldName);
        params.remove(AxinValidate.checkResultFieldName);
        return mapOb;
    }

    /**
     * 将校验失败结果Map的key映射为变量的中文名
     * {"变量中文名":"不通过校验的原因"}
     * @param checkResult
     * @param validateConfig
     * @return
     */
    public static Map<String, String> convertNameCheckResult(Map<String, String> checkResult, ValidateConfig validateConfig) {
        Map<String, String> checkResultName = new LinkedHashMap<>();
        for (Map.Entry<String, String> result : checkResult.entrySet()) {
            for (FieldCheckUnit field : validateConfig.getFields()) {
                if (field.getFieldCode().equals(result.getKey())) {
                    checkResultName.put(field.getFieldName(), result.getValue());
                    break;
                }
            }
        }
        return checkResultName;
    }

    /**
     * 跳出检查-可在 checkBefore 或 checkAfter方法中使用 终止当前检查
     */
    public static void skipCheck() {
        throw new ValidateSkipException("跳出检查");
    }

    /**
     * 获得对象的字符串
     * @param value
     * @return
     */
    public static String getStringValue(Object value) {
        if (value instanceof String) {
            return value.toString();
        }
        return JSON.toJSONString(value);
    }


    //——————————————————————变量检查工具————————————————————————————

    //必填检查
    public static void fieldRequireCheck(String field, Object value, FieldCheckUnit unit, Map<String, String> checkResult) {

        if (unit.getRequire() && StringUtils.isEmpty(value)) {
            String err = String.format("[%s]是必填字段", unit.getFieldName());
            checkResult.put(field, err);
        }
    }

    //变量类型检查
    public static void fieldTypeCheck(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (value == null) {
            return;
        }

        FieldType type = FieldType.getFieldType(unit.getFieldType());

        if (!type.getCheckEngine().test(value)) {
            checkResult.put(field, type.getErrMsg().apply(unit.getFieldName(), value));
        }
    }

    //字符串检查
    public static void fieldStrCheck(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (value == null || !unit.getFieldType().equals(FieldType.String.getType())) {
            return;
        }

        List<String> result = new ArrayList<>();
        if (unit.getMaxLen() != null && value.length() > unit.getMaxLen()) {
            String err1 = String.format("[%s]的值[%s]的长度大于[%s]", unit.getFieldName(), value, unit.getMaxLen());
            result.add(err1);
        }
        if (unit.getMinLen() != null && value.length() < unit.getMinLen()) {
            String err2 = String.format("[%s]的值[%s]的长度小于[%s]", unit.getFieldName(), value, unit.getMaxLen());
            result.add(err2);
        }
        if (unit.getRegular() != null && !isMatch(unit.getRegular(), value)) {
            String err3 = String.format("[%s]的值[%s]不满足正则表达式[%s]", unit.getFieldName(), value, unit.getRegular());
            result.add(err3);
        }

        if (!CollectionUtils.isEmpty(result)) {
            checkResult.merge(field, result.stream().collect(Collectors.joining(",")), (oldV, newV) -> oldV + "," + newV);
        }
    }

    //数字检查
    public static void fieldNumberCheck(String field, String value, FieldCheckUnit unit, Map<String, String> checkResult) {
        if (value == null || !unit.getFieldType().equals(FieldType.Number.getType())) {
            return;
        }
        if (!isNumber(value)) {
            return;
        }
        List<String> result = new ArrayList<>();
        BigDecimal number = new BigDecimal(value);
        BigDecimal maxSize = null;
        BigDecimal minSize = null;
        BigDecimal notEqual = null;
        if (unit.getMaxSize() != null) {
            maxSize = new BigDecimal(unit.getMaxSize().toString());
        }
        if (unit.getMinSize() != null) {
            minSize = new BigDecimal(unit.getMinSize().toString());
        }
        if (unit.getNotEqual() != null) {
            notEqual = new BigDecimal(unit.getNotEqual().toString());
        }

        if (unit.getMaxSize() != null && number.compareTo(maxSize) == 1) {
            String err1 = String.format("[%s]的值[%s]大于[%s]", unit.getFieldName(), number, maxSize);
            result.add(err1);
        }
        if (unit.getMinSize() != null && number.compareTo(minSize) == -1) {
            String err2 = String.format("[%s]的值[%s]小于[%s]", unit.getFieldName(), number, minSize);
            result.add(err2);
        }
        if (unit.getNotEqual() != null && number.compareTo(notEqual) == 0) {
            String err4 = String.format("[%s]的值[%s]不能等于[%s]", unit.getFieldName(), number, notEqual);
            result.add(err4);
        }

        if (unit.getDecimalPlace() != null && !decimalPlaceCheck(number.toString(), unit.getDecimalPlace())) {
            String err3 = String.format("[%s]的值[%s]小数位超过[%s]位", unit.getFieldName(), number.toString(), unit.getDecimalPlace());
            result.add(err3);
        }

        if (!CollectionUtils.isEmpty(result)) {
            checkResult.merge(field, result.stream().collect(Collectors.joining(",")), (oldV, newV) -> oldV + "," + newV);
        }
    }

    //————————————————————————————————————类型判断工具——————————————————————————————————————

    /**
     * 给定内容是否匹配正则
     *
     * @param regex   正则
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    private static boolean isMatch(String regex, CharSequence content) {
        if (content == null) {
            // 提供null的字符串为不匹配
            return false;
        }

        if (StringUtils.isEmpty(regex)) {
            // 正则不存在则为全匹配
            return true;
        }
        //Pattern.DOTALL模式, 会匹配所有字符包括换行符
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return pattern.matcher(content).matches();
    }

    /**
     * 判断字符串是否是时间戳<br>
     * @param s String
     */
    public static boolean isTimeStamp(String s) {
        try {
            if (TypeUtils.castToDate(s) == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是数字
     *
     * @param s String
     * @return 是否为{@link Double}类型
     */
    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ignore) {
        }
        return false;
    }

    /**
     * 判断数字小数位是否超过place位
     * @param number
     * @return
     */
    private static boolean decimalPlaceCheck(String number, Integer place) {
        if (!number.contains(".")) {
            return true;
        }
        String[] number2 = number.split("\\.");
        if (number2[1].length() > place) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException ignore) {
        }
        return false;
    }

    /**
     * 判断字符串是否是布尔
     *
     * @param s String
     */
    public static boolean isBoolean(String s) {
        if ("true".equals(s) || "false".equals(s)) {
            return true;
        }
        return false;
    }

    //————————————————————————————存在性校验工具—————————————————————————————————
    /**
     * 获取枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序<br>
     * 结果中键为枚举名，值为枚举对象
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @return 枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序
     * @since 4.0.2
     */
    public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(final Class<E> enumClass) {
        final LinkedHashMap<String, E> map = new LinkedHashMap<String, E>();
        for (final E e : enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }

    public static boolean cityCheck(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        if (lookUpValue(buildCacheKey(FieldExist.城市.getCode(), code)) == null) {
            String value = "从其他系统查询方法获得的value";
            if (value == null) {
                return false;
            }
            putCache(buildCacheKey(FieldExist.城市.getCode(), code), value);
        }
        return true;
    }

    /**
     * 配置单元检查
     * @param field
     * @param unit
     */
    public static void configCheck(String field, FieldCheckUnit unit) {
        List<String> result = new ArrayList<>();

        if (unit == null) {
            String err1 = String.format("校验配置文件格式错误！[%s]的校验单元没有配置！", field);
            throw new ValidateConfigException(err1);
        }
        if (unit.getRequire() == null) {
            String err2 = String.format("校验配置文件格式错误！[%s]:[%s]的是否必填require必须配置！", field, unit.getFieldName());
            result.add(err2);
        }
        if (!StringUtils.hasText(unit.getFieldType())) {
            String err3 = String.format("校验配置文件格式错误！[%s]:[%s]的变量类型fieldType必须配置！", field, unit.getFieldName());
            result.add(err3);

        }
        if (FieldType.getFieldType(unit.getFieldType()) == null) {
            String err4 = String.format("校验配置文件格式错误！[%s]:[%s]的变量类型fieldType配置错误！", field, unit.getFieldName());
            result.add(err4);
        }
        if (!CollectionUtils.isEmpty(result)) {
            throw new ValidateConfigException(result.stream().collect(Collectors.joining(",")));
        }

    }

    private static String buildCacheKey(String pre, String code) {
        return pre.concat(":").concat(code);
    }

    //——————————————————————————初始化工具——————————————————————————————

    public static void setCache(AxinValidateCacheProperties properties) {
        //未开启
        enable = properties.isEnable();
        if (!properties.isEnable()) {
            return;
        }

        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        long expireAfterAccess = properties.getExpireAfterAccess();
        long expireAfterWrite = properties.getExpireAfterWrite();
        int initialCapacity = properties.getInitialCapacity();
        long maximumSize = properties.getMaximumSize();
        long refreshAfterWrite = properties.getRefreshAfterWrite();
        log.debug("校验组件进程缓存初始化：");
        if (expireAfterAccess > 0) {
            log.debug("设置本地缓存访问后过期时间，{}秒", expireAfterAccess);
            cacheBuilder.expireAfterAccess(expireAfterAccess, TimeUnit.SECONDS);
        }
        if (expireAfterWrite > 0) {
            log.debug("设置本地缓存写入后过期时间，{}秒", expireAfterWrite);
            cacheBuilder.expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS);
        }
        if (initialCapacity > 0) {
            log.debug("设置缓存初始化大小{}", initialCapacity);
            cacheBuilder.initialCapacity(initialCapacity);
        }
        if (maximumSize > 0) {
            log.debug("设置本地缓存最大值{}", maximumSize);
            cacheBuilder.maximumSize(maximumSize);
        }
        if (refreshAfterWrite > 0) {
            cacheBuilder.refreshAfterWrite(refreshAfterWrite, TimeUnit.SECONDS);
        }
        cacheBuilder.recordStats();
        localCache = cacheBuilder.build();
    }


    //——————————————————————————进程缓存工具——————————————————————————
    /**
     * 从缓存取值
     * @param code
     * @return
     */
    public static Object lookUpValue(String code) {
        if (!enable) {
            return null;
        }
        return localCache.getIfPresent(code);
    }

    /**
     * 向缓存put值
     * @param code
     * @param value
     */
    public static void putCache(String code, Object value) {
        if (!enable) {
            return;
        }

        localCache.put(code, value);
    }

    /**
     * 获取缓存命中率统计信息
     * @return
     */
    public static CacheStats getCacheHitStatistics() {
        if (localCache == null) {
            return null;
        }
        return localCache.stats();
    }

    /**
     * 清除组件进程缓存
     */
    public static void clearAllCache() {
        if (localCache == null) {
            return;
        }
        localCache.invalidateAll();
    }
}
