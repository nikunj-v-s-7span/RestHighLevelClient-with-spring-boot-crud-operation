package com.employeeManagement.employeeManagement.controller;


import com.employeeManagement.employeeManagement.model.Employee;
import com.employeeManagement.employeeManagement.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
public class ElasticSearchController {

//    @Autowired
//    private EmployeeRepository employeeServiceImpl;

    @Autowired
    public EmployeeServiceImpl employeeServiceImpl;
    //USE Below API for insert Dummy Employee data size is 50
    List<String> role = Arrays.asList("Java", "Node", "React", "dotNet", "Mobile", "Design", "Laravel", "Magento", "workDay", "JavaScript", "QA");

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
    public ResponseEntity<Object> searchAllDocument(@RequestParam(required = false) Integer size) throws IOException {
        List<Employee> employees = employeeServiceImpl.fetchEmployeeList(size);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/customSearch")
    public ResponseEntity<Object> customEmployeeSearch(@RequestParam(required = false) Integer min_age, @RequestParam(required = false) Integer max_age, @RequestParam(required = false) Double min_salary, @RequestParam(required = false) Double max_salary, @RequestParam(required = false) Boolean isTrainee) throws IOException {
        List<Employee> employees = employeeServiceImpl.fetchCustomSearchEmployee(min_age, max_age, min_salary, max_salary, isTrainee);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/bulkInsert")
    public void bulkInsert() throws IOException {
        for (int i = 0; i < 50; i++) {
            Employee employee = new Employee();
            employee.setId(i + "");

            Random random = new Random();
            int randomNumber = random.nextInt(51 - 17) + 17;
            employee.setEmployeeAge(randomNumber);
            employee.setIsTrainee(random.nextBoolean());
            employee.setName("Emp_" + i);
            employee.setRole(role.get(random.nextInt(11 - 0) + 0));
            employee.setSalary(random.nextInt(56000 - 25000) + 25000);
            employeeServiceImpl.upsertEmployee(employee);
        }
    }
}