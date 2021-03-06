package com.zefu.mq.excutor;

import com.alibaba.fastjson.JSONObject;
import com.zefu.business.api.RemoteBusComService;
import com.zefu.business.api.RemoteProductFuncService;
import com.zefu.business.api.RemoteProductService;
import com.zefu.common.base.biz.EsUtil;
import com.zefu.common.base.biz.TopicBiz;
import com.zefu.common.base.constants.Constants;
import com.zefu.common.base.domain.bo.BaseAttrItemBo;
import com.zefu.common.base.domain.dto.request.ProductFuncItemResDto;
import com.zefu.common.base.domain.gateway.mq.PropertySetMessageBo;
import com.zefu.common.base.domain.message.DeviceDownMessage;
import com.zefu.common.base.domain.storage.EsMessage;
import com.zefu.common.base.enums.ErrorEnum;
import com.zefu.common.base.exception.GException;
import com.zefu.common.base.metadata.EsInsertDataBo;
import com.zefu.common.base.metadata.MetaType;
import com.zefu.common.base.metadata.ProductFuncTypeEnum;
import com.zefu.common.core.utils.bus.JSONProvider;
import com.zefu.common.protocol.base.IBaseProtocol;
import com.zefu.common.protocol.service.IProtocolUtilService;
import com.zefu.common.redis.service.RedisService;
import com.zefu.common.storage.service.IDataCenterService;
import com.zefu.mq.mqttclient.PubMqttClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class PropertySetExecutor {

    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    RemoteProductFuncService productFuncService;
    @Autowired
    RedisService cacheTemplate;
    @Autowired
    RemoteProductService productService;
    @Autowired
    IProtocolUtilService protocolUtilService;
    @Autowired
    PubMqttClient pubMqttClient;
    @Autowired
    RemoteBusComService remoteBusComService;


    /***
     * 具体线程
     */
    @Async(Constants.TASK.PROPERTY_SET_NAME)
    public void execute(PropertySetMessageBo msg) {
        try {
            remoteBusComService.todayTotalIncr();
            this.rebuildMsg(msg);
            IBaseProtocol protocolService = protocolUtilService.queryProtocolInstanceByCode(msg.getProtocolCode());
            /** 来自设备转化为json对象的消息实体 */
            EsInsertDataBo dataBo = this.parseMsg(msg, msg.getCommand());
            byte[] pubMsg = protocolService.encode(msg.getTopic(), msg.getDeviceCode(), this.buildDownMessage(dataBo));
            this.saveData2Es(dataBo);
            pubMqttClient.publish(msg.getTopic(), pubMsg);
        } catch (Exception e) {
            log.warn("内部队列消费异常", e);

        }

    }
    private void saveData2Es(EsInsertDataBo dataBo){
        Boolean ret = remoteBusComService.lockProp(dataBo.getEsMessage().getMessageId()).getData();
        if(ret){
            dataCenterService.saveData(dataBo);
        }
    }
    /** 组装下发给设备的消息 */
    private final DeviceDownMessage buildDownMessage(EsInsertDataBo dataBo) {
        DeviceDownMessage deviceDownMessage = new DeviceDownMessage();
        deviceDownMessage.setMessageId(dataBo.getEsMessage().getMessageId());
        deviceDownMessage.setTimestamp(System.currentTimeMillis());
        deviceDownMessage.setBody(dataBo.getEsMessage().getRequest());
        deviceDownMessage.setIdentifier(dataBo.getIdentifier());
        deviceDownMessage.setDeviceCode(dataBo.getEsMessage().getDeviceCode());
        deviceDownMessage.setProductCode(dataBo.getEsMessage().getProductCode());
        return deviceDownMessage;
    }

    private void rebuildMsg(PropertySetMessageBo msg) {
        String topic = TopicBiz.buildPropertySet(msg.getDeviceCode(), msg.getProductCode());
        msg.setTopic(topic);
        msg.setFuncType(ProductFuncTypeEnum.PROP);
        msg.setProtocolCode(productService.queryProtocolCodeByCode(msg.getProductCode()).getData());
    }

    /**
     * 将协议解析后得到的json数据按照产品属性进行重新组装
     */
    private EsInsertDataBo parseMsg(PropertySetMessageBo reqMsg, JSONObject json) {
        Object finalResult = null;
        EsInsertDataBo dataBo = new EsInsertDataBo();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ProductFuncItemResDto funcItemResDto =
                productFuncService.queryFunc(reqMsg.getProductCode(), reqMsg.getFuncType(), reqMsg.getIdentifier()).getData();

            /** 下发给设备的数据 */
            BaseAttrItemBo attrItemBo =
                JSONProvider.parseObjectDefValue(funcItemResDto.getAttr(), BaseAttrItemBo.class);
            /** 不是结构的话，这里就是简单类型，无法转换为map */
            /** 数据库中存储的改字段下的标识符列表 */
            if (MetaType.STRUCT.getCode().equals(funcItemResDto.getDataType())) {
                resultMap = this.parseStructMsg(reqMsg, attrItemBo, json);
                finalResult = resultMap;
            } else {
                // 参数是简单数据或者没有参数
                resultMap = this.parseSampleMsg(reqMsg, attrItemBo, json);
                finalResult = resultMap.get(reqMsg.getIdentifier());
            }
            String indexName =
                EsUtil.buildDevPropertyIndex(reqMsg.getFuncType(), reqMsg.getProductCode(), reqMsg.getIdentifier());
            dataBo.setIndexName(indexName);
            EsMessage esMessage = new EsMessage();
            esMessage.setRequest(finalResult);
            esMessage.setDeviceCode(reqMsg.getDeviceCode());
            esMessage.setProductCode(reqMsg.getProductCode());
            esMessage.setMessageId(reqMsg.getMessageId());
            esMessage.setTimestamp(reqMsg.getCurrTime());
            dataBo.setIdentifier(reqMsg.getIdentifier());
            dataBo.setEsMessage(esMessage);
        } catch (Exception e) {
            log.warn("异常", e);
            return dataBo;
        }
        return dataBo;
    }

    private final Map<String, Object> parseSampleMsg(PropertySetMessageBo reqMsg, BaseAttrItemBo attrItemBo,
        JSONObject json) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!attrItemBo.getIdentifier().equals(reqMsg.getIdentifier())) {
            throw GException.genException(ErrorEnum.INVALID_PARAM, "指令非法");
        }
        Object value = json.getObject(reqMsg.getIdentifier(), Object.class);
        Object retrieveVal = value;

        resultMap.put(attrItemBo.getIdentifier(), retrieveVal);
        return resultMap;

    }

    /**
     * 解析内容非结构体的下发指令
     */
    private final Map<String, Object> parseStructMsg(PropertySetMessageBo reqMsg, BaseAttrItemBo attrItemBo,
        JSONObject json) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, BaseAttrItemBo> attrMap = new HashMap<>();
        for (BaseAttrItemBo bo : attrItemBo.getData()) {
            attrMap.put(bo.getIdentifier(), bo);
        }

        Map<String, Object> dataMap =
            (Map)JSONProvider.parseJsonObject(json.getJSONObject(reqMsg.getIdentifier()), Map.class);
        dataMap.forEach((key, value) -> {
            Object retrieveVal = value;
            if (attrMap.containsKey(key)) {
                resultMap.put(key, retrieveVal);
            }
        });
        return resultMap;
    }
}
