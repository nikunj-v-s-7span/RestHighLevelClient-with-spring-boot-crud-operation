package com.employeeManagement.employeeManagement.service;

import com.employeeManagement.employeeManagement.model.Employee;
import com.employeeManagement.employeeManagement.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    //  Document Index name in elastic search
    private final String indexName = "employees";

    @Autowired
    private EmployeeRepository employeeRepository;
    //    ObjectMapper for mapping Object to Map and Map to Object
    @Autowired
    private ObjectMapper objectMapper;

//  Upsert Request method for (Insert or Update)
//  If Document Present with giver id then it will update or if not present any document with this id then it will create new

    @Override
    public String upsertEmployee(Employee employee) throws IOException {
        //  Mapping Employee to Map
        Map<String, Object> doc = objectMapper.convertValue(employee, Map.class);
//  Index Request for perform Operation on Particular index(Only Insert and Update)
        IndexRequest request = new IndexRequest(indexName).id(employee.getId()).source(doc);
//  Fetch response from IndexRequest

        IndexResponse response = employeeRepository.createOrUpdateEmployee(request);
        //  Return Message base on IndexResponse
        if (response.getResult() == DocWriteResponse.Result.CREATED) {
            return "Data Inserted Successfully";
        } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
            return "Data Updated Successfully";
        }
        return "Data Operation Fail";
    }

    //  Get All Data from Giver index
    @Override
    public List<Employee> fetchEmployeeList(Integer size) throws IOException {
        //  SearchRequest for Search Data from Elastic
        SearchRequest searchRequest = new SearchRequest(indexName);
//      SearchSourceBuilder use for build Custom search quest
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//To Fetch All Document from elaticsearch first we need total document size.
//        without this size elasticsearch search query only return first 10 records.
        if (size == null) {
            CountRequest countRequest = new CountRequest(indexName);
            CountResponse allDataCount = employeeRepository.getAllDataCount(countRequest);
            size = (int) allDataCount.getCount();
        }
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = employeeRepository.getAllEmployee(searchRequest);
        List<Employee> userList = new ArrayList<>();
        //  Check if Hits.Hits is not empty, if getTotalHits().value is 0 then search result is 0.
        if (searchResponse.getHits().getTotalHits().value > 0) {
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHit) {
                Map<String, Object> map = hit.getSourceAsMap();
                userList.add(objectMapper.convertValue(map, Employee.class));
            }
        }
        return userList;
    }

    //  Get Document By Given id
    @Override
    public Object fetchEmployeeById(String employeeId) throws IOException {
//  GetRequest for get any Document from elasticsearch
        GetRequest request = new GetRequest(indexName).id(employeeId);
        GetResponse response = employeeRepository.getEmployeeById(request);
        //  response.getSourceAsMap() return Map if our document will present in Elastic otherwise it will be null
        if (response.getSourceAsMap() == null) {
            return "Data Not Available";
        }
        return objectMapper.convertValue(response.getSourceAsMap(), Employee.class);
    }

    @Override
    public String deleteEmployeeById(String employeeId) throws IOException {
        //  For perform Delete operation on Elastic there is DeleteRequest
        DeleteRequest request = new DeleteRequest(indexName).id(employeeId);
        DeleteResponse response = employeeRepository.deleteEmployeeById(request);
        //  responce.getResult() return Result status base on operation
        if (response.getResult() == DocWriteResponse.Result.DELETED) {
            return "Data Deleted Successfully";
        }
        return "Data not found";
    }

    @Override
    public List<Employee> fetchCustomSearchEmployee(Integer min_age, Integer max_age, Double min_salary, Double max_salary, Boolean isTrainee) throws IOException {
        BoolQueryBuilder query2 = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("employeeAge").gte(min_age).lte(max_age)).must(QueryBuilders.rangeQuery("salary").gte(min_salary).lte(max_salary));
        BoolQueryBuilder query1 = null;
        if (isTrainee != null) {
            query1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("isTrainee", isTrainee));
        }
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query1).query(query2);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = employeeRepository.getAllEmployee(searchRequest);

        List<Employee> userList = new ArrayList<>();
        //  Check if Hits.Hits is not empty, if getTotalHits().value is 0 then search result is 0.
        if (response.getHits().getTotalHits().value > 0) {
            SearchHit[] searchHit = response.getHits().getHits();
            for (SearchHit hit : searchHit) {
                Map<String, Object> map = hit.getSourceAsMap();
                userList.add(objectMapper.convertValue(map, Employee.class));
            }
        }
        return userList;
    }


}
