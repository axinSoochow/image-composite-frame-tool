package com.axin.idea.validate.domain;


import lombok.Data;

import java.util.List;


/**
 * 数组校验单元
 */
@Data
public class ListCheckUnit {

    /** list的变量名 */
    private String listKey;
    /** 元素 */
    private List<FieldCheckUnit> fields;

}
