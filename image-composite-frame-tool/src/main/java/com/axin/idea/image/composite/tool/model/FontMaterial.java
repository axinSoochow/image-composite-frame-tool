package com.axin.idea.image.composite.tool.model;

import com.axin.idea.image.composite.tool.model.enumm.ImageFontStyle;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Axin
 * @summary 字体素材 目前只能生成黑色字体
 * 暂不支持换行
 * @since 2021-01-15
 */
@Data
public class FontMaterial {

    /**
     * 文字内容
     */
    @NotBlank(message = "文字素材的text参数不能为空！")
    private String text;

    /**
     * 字体类型
     * 例如，微软雅黑。
     * 这个值与你操作系统已安装的字体有关，使用前请查询节点所在的操作系统是否安装了你传入的字体类型
     */
    @NotBlank(message = "文字素材的fontType参数不能为空！")
    private String fontType;

    /**
     * 字体样式
     */
    @NotNull(message = "文字素材的fontStyle参数不能为空！")
    private ImageFontStyle fontStyle;

    /**
     * 行间距 默认为5
     */
    private Integer lineDistance;

    /**
     *  字体大小
     */
    private int fontSize;

}
