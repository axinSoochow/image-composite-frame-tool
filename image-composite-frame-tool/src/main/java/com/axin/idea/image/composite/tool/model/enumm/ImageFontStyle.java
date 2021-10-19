package com.axin.idea.image.composite.tool.model.enumm;

import lombok.Getter;

/**
 * @author Axin
 * @summary 图片字体样式
 * @since 2021-01-15
 */
@Getter
public enum ImageFontStyle {
    普通(0, "普通字体"),
    加粗(1, "字体加粗"),
    斜体(2, "斜体"),
    ;

    private int code;

    private String desc;

    ImageFontStyle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
