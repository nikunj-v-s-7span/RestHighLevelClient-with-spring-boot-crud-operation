package com.employeeManagement.employeeManagement.controller;


import com.employeeManagement.employeeManagement.model.Employee;
import com.employeeManagement.employeeManagement.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

//    @Autowired
//    private EmployeeRepository employeeServiceImpl;

    @Autowired
    public EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("/upsertEmployee")
    public ResponseEntity<Object> createOrUpdateDocument(@RequestBody Employee employee) throws IOException {
        String response = employeeServiceImpl.upsertEmployee(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getEmployee")
    public ResponseEntity<Object> getDocumentById(@RequestParam String employeeId) throws IOException {
        return new ResponseEntity<>(employeeServiceImpl.fetchEmployeeById(employeeId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Object> deleteDocumentById(@RequestParam String employeeId) throws IOException {
        String response = employeeServiceImpl.deleteEmployeeById(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchEmployee")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Employee> employees = employeeServiceImpl.fetchEmployeeList();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}