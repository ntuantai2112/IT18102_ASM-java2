/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.utils;

import com.poly.model.Employee;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class XFile {

    private static Object ObjectInput;

    

    // Tạo hàm lưu đối tượng bất kì vào trong file và sẽ đọc ghi ra
    public static Object readObject(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (
                 FileInputStream fis = new FileInputStream(path); 
                 ObjectInputStream objInput = new ObjectInputStream(fis);
               
             ){
                 return objInput.readObject();
        }
    }
    // sử dụng try()with resurch đảm bảo cho chúng ta, tài nguyên sẽ được đóng sau khi thực hiện câu lệnh   
    public static void writeObject(String path, Object data ) throws FileNotFoundException, IOException{
        try(
               FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream objOutput = new ObjectOutputStream(fos);
             ){
            // Object data là dữ liệu cần truyền vào file 
            objOutput.writeObject(data);
        }
    }
}
