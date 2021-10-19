package com.axin.idea.image.composite.tool.model.enumm;

import lombok.Getter;

/**
* @author Axin
* @since 2021-01-15
* @summary 图像合成类型
*/
@Getter
public enum ImageType {
    PNG("png","便携式网络图形(Portable Network Graphics)"),
    JPG("jpg","联合图像专家组(Joint Photographic Experts Group)"),
    GIF("gif","图形交换格式(Graphics Interchange Format)")
    ;

    private String code;

    private String desc;

    ImageType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
