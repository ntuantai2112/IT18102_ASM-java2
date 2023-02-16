/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.service;

import com.poly.model.Employee;
import java.util.ArrayList;

/**
 *
 * @author LENOVO T560
 */
public class EmployeeService implements IEmployeeService{
    ArrayList<Employee> list = new ArrayList<>();
    @Override
    public void addEmployee(Employee em) {
        list.add(em);
    }

    @Override
    public void updateEmployee(int id, Employee em) {
        list.set(id, em);
    }

    @Override
    public void deleteEmployee(int id) {
        list.remove(id);
    }

    @Override
    public ArrayList<Employee> select() {
        return this.list;
    }

    @Override
    public void setList(ArrayList<Employee> ds) {
        this.list = ds;
    }
    
}
