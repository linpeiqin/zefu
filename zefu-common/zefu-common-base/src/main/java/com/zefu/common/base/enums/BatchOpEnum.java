package com.zefu.common.base.enums;

import lombok.Getter;


@Getter
public enum BatchOpEnum {
    DISABLE("DISABLE", "禁用"),
    ENABLE("ENABLE", "启用"),
    DELETE("DELETE", "删除"),
    ONLINE("ONLINE", "上线"),
    OFFLINE("OFFLINE", "下线"),;
    String type;
    String msg;

    BatchOpEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static BatchOpEnum explain(String type){
        for(BatchOpEnum item:BatchOpEnum.values()){
            if(item.type.equals(type)){
                return item;
            }
        }
        return null;
    }
}
