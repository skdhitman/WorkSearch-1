package com.example.demo.services;
import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.models.LogData;
import com.example.demo.repositories.LogDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogDataService
{
    @Autowired
    private LogDataRepository logDataRepository;

    
    // testing doc ingestion
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    private ObjectMapper mapper = new ObjectMapper();

//    @Override
//    public boolean create(IndexDto indexDto) throws IOException {
//        CreateIndexRequest request = new CreateIndexRequest(indexDto.getIndexName());
//        request.settings(Settings.builder()
//                .put("index.number_of_shards", indexDto.getNumberOfShards())
//                .put("index.number_of_replicas", indexDto.getNumberOfReplicas())
//        );
//        if (StrUtil.isNotEmpty(indexDto.getType()) && StrUtil.isNotEmpty(indexDto.getMappingsSource())) {
//            //mappings
//            request.mapping(indexDto.getType(), indexDto.getMappingsSource(), XContentType.JSON);
//        }
//        CreateIndexResponse response = elasticsearchRestTemplate.getClient()
//                .indices()
//                .create(request, RequestOptions.DEFAULT);
//        
//        
//        // testin
//        (new ElasticsearchRestTemplate(restHighLevelClient)).getC
//        
//        return response.isAcknowledged();
//    }
    
    
    public LogData createLogDataIndex(final LogData logData)
    {
        return logDataRepository.save(logData);
    }

    public Iterable<LogData> createLogDataIndices(final List<LogData> logDataList)
    {
        return logDataRepository.saveAll(logDataList);
    }

    public List<LogData> getAllLogDataForHost (String host)
    {
        return logDataRepository.findByHost(host);
    }

    public LogData findById (String id)
    {
        return logDataRepository.findById(id).get();
    }

    public List<LogData> findBySearchTerm (String term)
    {
        return logDataRepository.findByMessageContaining(term);
    }
}
