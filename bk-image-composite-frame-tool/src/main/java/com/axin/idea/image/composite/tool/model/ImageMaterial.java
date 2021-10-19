package com.axin.idea.image.composite.tool.model;

import com.axin.idea.image.composite.tool.model.enumm.ImageType;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
* @author Axin
* @since 2021-01-15
* @summary 素材
*/
@Data
public class ImageMaterial {

    /**
     * 合成后的图片格式类型 可为空 默认.png
     */
    private ImageType afterCompositeImageType;

    /**
     * 图片素材 不能为空 且至少有2个 并且只有一个模板
     */
    @Size(min = 2, message = "图片素材至少2个")
    private List<ImageMaterialUnit> materialUnits;

}
