package com.zefu.common.es;


import com.zefu.common.es.config.enums.EsMetaType;
import com.zefu.common.es.domain.EsRange;
import com.zefu.common.es.domain.PropertiesItem;
import com.zefu.common.es.domain.SearchItem;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;


public interface SampleEsClient {
    /**
     * 创建索引模版
     * @param templateName String 模版名
     * @param indexPatterns List 命中的索引
     * @param properties Map 模版附加属性
     * @param indexAliases String 索引别名
     * @param router String 路由
     * @return true or false
     * */
    Boolean createIndexTemplate(String templateName,String indexAliases,String router,
                                List<String> indexPatterns , Map<String , PropertiesItem> properties);
    Boolean createIndex(String indexName, Map<String, EsMetaType> sourceMap);
    /**索引重命名*/
    Boolean reindex(String source, String target);
    Boolean dropIndex(String indexName);
    <T> Boolean   addDoc(String indexName, T data, String routing);
    /**
     * @param  index 索引
     * @param route 路由
     * @param y Y 泛型，查询条件
     * @param clazz 返回结果的类型
     * */
    public <Y, T> List<T> queryByCondition(String index, String route, Y y, Class<T> clazz) throws Exception;

    /**
     * @param  indices 索引
     * @param route 路由
     * @param conditionList
     * @param clazz 返回结果的类型
     * @param fromId
     * @param size
     * @param orderBy
     * @param sortName
     * @param rangeList
     * @return
     * @exception Exception 发生内部异常时
     * */
    public <T> List<T> searchByPage(String[] indices, String route, List<SearchItem> conditionList, Class<T> clazz,
                                    int fromId, int size, String sortName, SortOrder orderBy,
                                    List<EsRange> rangeList) throws Exception;

    /**
     * 计数
     * @param  indices 索引
     * @param route 路由
     * @param conditionList
     * @param rangeList
     * @return
     * @throws  Exception
     * */
    long count(String[] indices, String route, List<SearchItem> conditionList, List<EsRange> rangeList) throws Exception;


}
