package com.zefu.common.storage.service.impl;



import com.zefu.common.storage.service.IMessageReplyService;
import com.zefu.common.storage.entity.MessageReplyEntity;
import com.zefu.common.storage.es.MessageReplyRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MessageReplyServiceImpl implements IMessageReplyService {
    @Autowired
    MessageReplyRepository messageReplyRepository;
    @Autowired
    ElasticsearchRestTemplate restTemplate;

    @Override
    public void create(MessageReplyEntity entity) {
        messageReplyRepository.save(entity);
    }

    @Override
    public long count(Date start, Date end) {
        Criteria criteria = Criteria.where("timestamp").between(start, end);
        Query query = new CriteriaQuery(criteria);
        return restTemplate.count(query, MessageReplyEntity.class);
    }

    @Override
    public MessageReplyEntity queryByMessageId(String deviceCode, String messageId) {
        Criteria criteria = Criteria.where("messageId").is(messageId);
        criteria.and(
                Criteria.where("deviceCode").is(deviceCode)
        );
        Query query = new CriteriaQuery(criteria);
        SearchHits<MessageReplyEntity> result = restTemplate.search(query, MessageReplyEntity.class);
        if(result.getTotalHits() == 0L){
            return null;
        }
        Long timestamp = 0L;
        MessageReplyEntity resultEntity = null;
        for(SearchHit<MessageReplyEntity> item:result.getSearchHits()){
            MessageReplyEntity entity = item.getContent();
            if(timestamp < entity.getTimestamp().getTime()){
                resultEntity = entity;
                timestamp = entity.getTimestamp().getTime();
            }
        }
        return resultEntity;
    }

    @Override
    public List<MessageReplyEntity> searchHistory(String deviceCode, List<String> ids) {
        List<MessageReplyEntity> resultList = new ArrayList<>();
        String[] array = ids.toArray(new String[0]);
        Criteria criteria = Criteria.where("messageId").in(array);
        criteria.and(
                Criteria.where("deviceCode").is(deviceCode)
        );
        Query query = new CriteriaQuery(criteria);

        SearchHits<MessageReplyEntity> result = restTemplate.search(query, MessageReplyEntity.class);
        if(result.getTotalHits() == 0L){
            return resultList;
        }
        for(SearchHit<MessageReplyEntity> item:result.getSearchHits()){
            MessageReplyEntity entity = item.getContent();
            resultList.add(entity);
        }
        return resultList;
    }
}
