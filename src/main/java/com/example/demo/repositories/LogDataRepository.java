package com.example.demo.repositories;










import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.models.LogData;

import java.util.List;

public interface LogDataRepository extends ElasticsearchRepository<LogData, String>
{
    List<LogData> findByHost(String host);

    List<LogData> findByMessageContaining(String message);
}
