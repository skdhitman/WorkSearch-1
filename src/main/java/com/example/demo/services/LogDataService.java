package com.example.demo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.LogData;
import com.example.demo.repositories.LogDataRepository;

import java.util.List;

@Service
public class LogDataService
{
    @Autowired
    private LogDataRepository logDataRepository;

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
