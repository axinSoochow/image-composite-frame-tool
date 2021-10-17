package com.axin.idea.validate.domain;

import lombok.Data;

import java.util.List;

/**
 * @summary 校验配置
 */
@Data
public class ValidateConfig {

    /**
     * 配置Id
     */
    private String configId;

    private String desc;

    /**
     * 校验域 "变量名":{ "校验类型":"校验值"}
     * "fields": {
     *         "product_code": {
     *             "name": "物业编码",
     *             "fieldType": "Date",//变量类型
     *             "maxLen": 20,//字符串最大长度
     *             "minLen": 10,//字符串最小长度
     *             "maxSize": 20,//数字大小
     *             "minSize": -1,
     *             "require": true,//必填
     *             "ext": "City",//存在性校验
     *             "regular":"[1-9]"//正则
     *         }
     *     }
     */
    private List<FieldCheckUnit> fields;

    /**
        listFileds:[
            {
                listKey:"List变量名";
                listType:"Object、String、Number、TimeStamp";//数组中元素类型
                fields:[
                {
                        fieldCode: "bizOrderNo",
                        fieldName: "业务订单号",
                        fieldType: "String",
                        require: false
                },
                {
                    fieldCode: "cityCode",
                        fieldName: "城市",
                        fieldType: "String",
                        ext: "City",
                        require: true
                }
                    ]
            }
        ]
     */
    private List<ListCheckUnit> listFields;

}
