package com.example.demo.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.models.LogData;
import com.example.demo.services.LogDataService;

@RestController
@RequestMapping("/logdata")
public class LogDataController
{
    @Autowired
    private LogDataService logDataService;

    @GetMapping("/host")
    public List<LogData> searchLogDataByHost(@RequestParam("host") String host)
    {
        List<LogData> logDataList = logDataService.getAllLogDataForHost(host);

        return logDataList;
    }

    @GetMapping("/search")
    public List<LogData> searchLogDataByTerm(@RequestParam("term") String term)
    {
        return logDataService.findBySearchTerm(term);
    }

    @PostMapping("/addData")
    public LogData addLogData(@RequestBody LogData logData)
    {

        return logDataService.createLogDataIndex(logData);
    }

    @PostMapping("/createInBulk")
    public  List<LogData> addLogDataInBulk(@RequestBody List<LogData> logDataList)
    {
        return (List<LogData>) logDataService.createLogDataIndices(logDataList);
    }
   
 
    
    	
    
}
    
  


