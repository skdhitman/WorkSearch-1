package com.example.demo.controllers;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
//import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
//import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.LogData;
import com.example.demo.services.LogDataService;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

@RestController
@RequestMapping("/logdata")
public class LogDataController
{
    private static final String MESSAGE_FIELD = "message";

	private static final String ROCKS = "rocks";

	private static final String TWITTER = "twitter1";

	@Autowired
    private LogDataService logDataService;
    
    @Autowired
    private RestHighLevelClient elasticSearchClient;
	
	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));


    // tetsing -----------------------------------------------------
    
    @GetMapping("/finals")
	public String trail1() throws IOException

	{

		//String filePath = "C:\\Users\\Hp\\Desktop\\phonenumber.txt";
		String filePath = "D:\\WORK\\NEOSOFT\\Java-Team\\Projects\\elastic-search\\store\\demodata.txt";
		
		String encodedfile = null;

		File file = new File(filePath);
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.getEncoder().encodeToString(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(encodedfile);
		// creating index
		CreateIndexRequest request = new CreateIndexRequest(TWITTER);
		CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		System.out.print(createIndexResponse);

		// mapping properties
		PutMappingRequest request2 = new PutMappingRequest(TWITTER);

		request2.source("{\n" + "  \"properties\": {\n" + "    \"message\": {\n" + "      \"type\": \"binary\"\n"
				+ "    }\n" + "  }\n" + "}", XContentType.JSON);
		org.elasticsearch.action.support.master.AcknowledgedResponse putMappingResponse = client.indices().putMapping(request2, RequestOptions.DEFAULT);

		//IndexRequest request3 = new IndexRequest("twitter", "_doc", "56");
		org.elasticsearch.action.index.IndexRequest request3 = new org.elasticsearch.action.index.IndexRequest(TWITTER, "_doc", "56");
		
		request3.source("{\n" + "  \"message\": " + "\"" + encodedfile + "\"" + "}", XContentType.JSON);

		org.elasticsearch.action.index.IndexResponse createIndexResponse1 = client.index(request3, RequestOptions.DEFAULT);
		
		System.out.println("Ingestion result >>> "+createIndexResponse1.toString());
		
		return createIndexResponse1.toString();

	}
    
    @GetMapping("/test-doc-ingestion")
    public String testDocIngestion() {
    	SearchRequest searchRequest = new SearchRequest(); 
    	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
    	searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
    	searchRequest.source(searchSourceBuilder);
    	
    	SearchResponse searchResponse = null;
    	try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			System.out.println("Hits >>> "+searchResponse.getHits());
			System.out.println("searchResponse >>> "+searchResponse.toString());
//			System.out.println(searchResponse.);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return searchResponse.toString();
    }

    @GetMapping("/search-with-term")
    public String searchWithTerm() {
    	SearchRequest searchRequest = new SearchRequest("logdataindex"); 
    	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
    	
//    	searchSourceBuilder.query(QueryBuilders.fuzzyQuery("message", "rocks"));
//    	searchSourceBuilder.query(QueryBuilders.commonTermsQuery("host", "neosoft"));
    	searchSourceBuilder.query(QueryBuilders.matchBoolPrefixQuery(MESSAGE_FIELD, "neo"));
    	
    	searchRequest.source(searchSourceBuilder);
    	
    	SearchResponse searchResponse = null;
    	try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			System.out.println("Hits >>> "+searchResponse.getHits());
			System.out.println("searchResponse >>> "+searchResponse.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return searchResponse.toString();
    }

    
    
    
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
    
  


