/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.model;

import java.io.Serializable;

/**
 *
 * @author LENOVO T560
 */
public class Employee implements Serializable{
    private String maNV,hoTen,email;
    private double luong;
    private int age;

    public Employee() {
    }

    public Employee(String maNV, String hoTen, int age, String email, double luong) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.email = email;
        this.luong = luong;
        this.age = age;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    
}
