package com.ke.fmp.book.crm.export;

import com.ke.bfa.core.util.enumm.BaseEnum;

/**
 * @author: yusenhua
 * @date: 2020/5/13 18:00
 * @description: crm 导出类型枚举   DESC organCode + "_" + bizCategory + "_" + exportSubType
 */
public enum CRMEnum implements BaseEnum {

    用户纠纷台账("10002","CRM_904000001_1002");

    CRMEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
     }

    private String code;

    private String desc;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static CRMEnum getEnumByDesc(String desc){
        for(CRMEnum crmEnum : CRMEnum.values()){
            if(desc.equalsIgnoreCase(crmEnum.getDesc())){
                return crmEnum;
            }
        }
        return null;
    }
}
