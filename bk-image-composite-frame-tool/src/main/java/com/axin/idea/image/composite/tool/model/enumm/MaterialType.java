package com.axin.idea.image.composite.tool.model.enumm;

import lombok.Getter;

/**
 * @author Axin
 * @summary 素材类型
 * @since 2021-01-15
 */
@Getter
public enum MaterialType {

    模板("template", "素材用于底板"),
    图片元素("pic", "图片素材"),
    文字元素("words", "文字素材"),
    ;

    private String code;

    private String desc;

    MaterialType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
