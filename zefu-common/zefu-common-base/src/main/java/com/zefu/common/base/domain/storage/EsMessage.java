package com.zefu.common.base.domain.storage;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zefu.common.base.metadata.MetaDesc;
import com.zefu.common.es.config.enums.EsMetaType;
import lombok.Data;

import java.util.Date;


@Data
public class EsMessage {
    @MetaDesc(code = "deviceCode", name = "设备编码", desc = "", type = EsMetaType.KEYWORD)
    String deviceCode;
    @MetaDesc(code = "productCode", name = "产品编码", desc = "", type = EsMetaType.KEYWORD)
    String  productCode;
    /**存入服务器时间,用来比对延迟*/
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss.SSS",timezone ="GMT+8")
    @MetaDesc(code = "saveTimestamp", name = "存入服务器时间", desc = "服务器处理到数据的时间", type = EsMetaType.DATE)
    Date    saveTimestamp;
    /**抵达服务器时候的服务器时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss.SSS",timezone ="GMT+8")
    @MetaDesc(code = "timestamp", name = "时间", desc = "抵达服务器时候的服务器时间", type = EsMetaType.DATE)
    Date    timestamp;
    /**设备发送消息的时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss.SSS",timezone ="GMT+8")
    @MetaDesc(code = "deviceTimestamp", name = "时间", desc = "设备发送消息的时间", type = EsMetaType.DATE)
    Date    deviceTimestamp;
    /**设备上传的消息ID*/
    @MetaDesc(code = "messageId", name = "messageId", desc = "", type = EsMetaType.KEYWORD)
    String  messageId;
    /**请求值,这里的值是单个属性的值
     * 当是设备上行消息时存的是设备的上行消息，这里的value是单个属性的值
     * 当是服务端发给设备的下行消息时，存的是下行消息实体
     * */
    @MetaDesc(code = "request", name = "request", desc = "", type = EsMetaType.UNKNOWN)
    Object request;
    /**返回值
     * 当时设备上行消息时，存的是服务端给设备的返回值(一般情况下都为空)
     * 当是服务端给设备发的下行消息时，存的是设备返回给服务端的响应实体(可能空)
     * */
    @MetaDesc(code = "response", name = "response", desc = "", type = EsMetaType.UNKNOWN)
    Object response;
}
