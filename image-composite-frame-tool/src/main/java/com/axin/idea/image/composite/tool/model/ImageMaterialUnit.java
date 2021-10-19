package com.axin.idea.image.composite.tool.model;

import com.axin.idea.image.composite.tool.model.enumm.MaterialType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Axin
 * @summary 图片合成单元
 * @since 2021-01-15
 */
@Data
public class ImageMaterialUnit {

    /**
     * 图片元素类型
     */
    @NotNull(message = "materialType必填")
    private MaterialType materialType;

    /**
     * x坐标
     */
    private int x;

    /**
     * y坐标
     */
    private int y;

    /**
     * 图片宽 非字体元素必填
     */
    private Integer width;

    /**
     * 图片高 非字体元素必填
     */
    private Integer height;

    /**
     * 文字素材
     */
    @Valid
    private FontMaterial fontMaterial;

    /**
     * 图片文件 优先
     */
    private File file;

    /**
     * 图片链接 图片素材 url 与file 至少传一个
     * 文字素材不用填
     */
    private String url;

    // ————————————— 以下参数无需关注 ————————————————
    /**
     * 临时图片缓存 无需传输
     */
    private BufferedImage tempImage;

}
