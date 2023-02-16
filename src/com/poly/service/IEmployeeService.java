/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poly.service;

import com.poly.model.Employee;
import java.util.ArrayList;

/**
 *
 * @author LENOVO T560
 */
public interface IEmployeeService {
    public void addEmployee(Employee em);
    public void updateEmployee(int id,Employee em);
    public void deleteEmployee(int id);
    public ArrayList<Employee> select();
    public void setList(ArrayList<Employee> ds);
}
