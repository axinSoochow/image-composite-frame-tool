package com.axin.idea.image.composite.tool;

import com.axin.idea.image.composite.tool.model.FontMaterial;
import com.axin.idea.image.composite.tool.model.ImageCompositeException;
import com.axin.idea.image.composite.tool.model.ImageMaterial;
import com.axin.idea.image.composite.tool.model.ImageMaterialUnit;
import com.axin.idea.image.composite.tool.model.enumm.ImageType;
import com.axin.idea.image.composite.tool.model.enumm.MaterialType;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Axin
 * @summary 合成参数相关工具
 * @since 2021-01-20
 */
public class CompositeParamsUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CompositeParamsUtils.class);

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static Integer 文字元素行间距 = 5;

    /**
     * 校验对象，并返回校验结果。
     *
     * @param t
     * @return
     */
    private static List<String> getValidateResult(Object t) {
        return validator.validate(t)
                .stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + ":" + constraintViolation.getMessage())
                .collect(Collectors.toList());
    }

    /**
     * 校验数组工具类
     *
     * @param list
     * @return
     */
    private static Map<String, List<String>> validateList(List list) {
        if (list == null || list.size() == 0) {
            return new HashMap<>();
        }
        Map<String, List<String>> validateResult = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> result = getValidateResult(list.get(i));
            if (result.size() > 0) {
                validateResult.put(String.format("数组第%s个元素校验不通过", i), result);
            }
        }
        return validateResult;
    }

    /**
     * 必填参数校验
     *
     * @param imageMaterial
     */
    public static void verifyParams(ImageMaterial imageMaterial) {
        log.debug("image-composite: params check start...");

        // 基础校验
        if (imageMaterial == null) {
            throw new ImageCompositeException("ImageMaterial参数异常!");
        }

        List<ImageMaterialUnit> imageMaterialUnits = Optional.ofNullable(imageMaterial).map(ImageMaterial::getMaterialUnits).orElse(null);
        if (imageMaterialUnits == null || imageMaterialUnits.size() == 0) {
            throw new ImageCompositeException("imageMaterialUnits不能为空！");
        }

        Map<String, List<String>> errMsg = validateList(imageMaterialUnits);
        if (errMsg != null && errMsg.size() > 0) {
            log.warn("imageMaterial参数异常！详情：{}", errMsg.toString());
            throw new ImageCompositeException("imageMaterial参数异常");
        }

        // 特殊校验
        List<String> errMsgExt = new ArrayList<>();

        int index = 0;

        List<ImageMaterialUnit> templateUnits = new ArrayList<>();
        for (ImageMaterialUnit unit : imageMaterialUnits) {
            if (unit.getMaterialType() == MaterialType.模板) {
                templateUnits.add(unit);
            }

            if (unit.getMaterialType() != MaterialType.文字元素) {
                if (unit.getWidth() == null || unit.getHeight() == null) {
                    errMsgExt.add(String.format("materialUnits的[%s]元素的宽高必填！", index));
                }

                if (unit.getFile() == null && (unit.getUrl() == null || "".equals(unit.getUrl()))) {
                    errMsgExt.add(String.format("materialUnits的[%s]元素的file与url必填其一！", index));
                }
            }

            if (unit.getMaterialType() == MaterialType.文字元素) {
                if (unit.getFontMaterial() == null) {
                    errMsgExt.add(String.format("materialUnits的[%s]元素的fontMaterial必填！", index));
                }
            }
            index++;
        }

        if (templateUnits.size() != 1) {
            errMsgExt.add(String.format("materialUnits参数有且只能有一个模板素材元素", index));
        }
        if (errMsgExt.size() > 0) {
            log.warn("imageMaterial参数异常！详情：{}", errMsgExt.toString());
            throw new ImageCompositeException("imageMaterial参数异常！");
        }
        log.debug("image-composite: params check end...");
    }

    /**
     * 默认值刷新
     *
     * @param imageMaterial
     */
    public static void defaultValueRefresh(ImageMaterial imageMaterial) {
        log.debug("image-composite: default params refresh...");
        List<ImageMaterialUnit> imageMaterialUnits = imageMaterial.getMaterialUnits();

        if (imageMaterial.getAfterCompositeImageType() == null) {
            imageMaterial.setAfterCompositeImageType(ImageType.PNG);
        }

        for (ImageMaterialUnit unit : imageMaterialUnits) {
            if (unit.getMaterialType() == MaterialType.文字元素) {
                FontMaterial fontMaterial = unit.getFontMaterial();
                if (fontMaterial.getLineDistance() == null) {
                    fontMaterial.setLineDistance(文字元素行间距);
                }
            }

        }
        log.debug("image-composite: default params end...");
    }

}
