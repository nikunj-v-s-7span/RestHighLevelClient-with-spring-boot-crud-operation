package com.employeeManagement.employeeManagement.model;

public class Employee {

    private String id;
    private String name;
    private String role;
    private Double salary;
    private Integer employeeAge;
    private Boolean isTrainee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Integer getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(Integer employeeAge) {
        this.employeeAge = employeeAge;
    }

    public Boolean getIsTrainee() {
        return isTrainee;
    }

    public void setIsTrainee(Boolean isTrainee) {
        this.isTrainee = isTrainee;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", salary=" + salary +
                ", employeeAge=" + employeeAge +
                ", isTrainee=" + isTrainee +
                '}';
    }

}