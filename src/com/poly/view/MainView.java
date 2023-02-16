/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.poly.view;

import com.poly.model.Employee;
import com.poly.service.EmployeeService;
import com.poly.service.IEmployeeService;
import com.poly.utils.XFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO T560
 */
public final class MainView extends javax.swing.JFrame implements Runnable {

    DefaultTableModel tblModel;
    private IEmployeeService emService = new EmployeeService();
    ArrayList<Employee> list = emService.select();
    // Mặc định là -1,là khi người dùng thêm mới Object NhanVien, khi click vào bảng index thay đổi 
    public int index = -1;
    private static final String P_EMAIL = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$";

    /**
     * Creates new form MainView
     */
    public MainView() {
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        //data();
        fillTable();
        getInfomation();
        Thread t = new Thread(this);
        t.start();

    }

    @Override
    public void run() {
        while (true) {

            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm a");
            tblDongHo.setText(format.format(now));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // Khởi tạo table

    public void initTable() {
        tblModel = (DefaultTableModel) tblNhanVien.getModel();
        tblModel.setColumnIdentifiers(new String[]{"Mã nhân viên", "Họ và Tên", "Tuổi", "Email", "Lương"});
    }

    // Khởi tạo giá trị
    public void data() {
        emService.addEmployee(new Employee("NV01", "Nguyễn Tuấn Tài", 18, "taintph29115@gmail.com", 5000));
        emService.addEmployee(new Employee("NV02", "Nguyễn Tuấn Tú", 27, "tuntph29116@gmail.com", 2000));
        emService.addEmployee(new Employee("NV03", "Nguyễn Minh Tuấn", 28, "tuanntph29117@gmail.com", 3000));
        emService.addEmployee(new Employee("NV04", "Tạ Thanh Tùng", 24, "tungntph29118@gmail.com", 4000));
        emService.addEmployee(new Employee("NV05", "Hà Văn Phong", 29, "phongntph29119@gmail.com", 6000));
    }

    // Đổ dữ liệu lên table
    public void fillTable() {
        // xóa dữ liệu cũ trong bảng
        list = emService.select();
        tblModel.setRowCount(0);
        // thêm dữ liệu vào trong table
        for (Employee em : list) {
            tblModel.addRow(new Object[]{em.getMaNV(), em.getHoTen(), em.getAge(), em.getEmail(), em.getLuong()});
        }
    }

    // tạo hàm reset lại form, nút btn view
    public void resetForm() {
        txtMaNV.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtEmail.setText("");
        txtSalary.setText("");
        index = -1;
    }

    // tạo hàm readForm() trả về tất cả  dữ liệu nhân viên nhập vào từ form
    public Employee readForm() {
        Employee em = new Employee(txtMaNV.getText(), txtName.getText(), Integer.valueOf(txtAge.getText()), txtEmail.getText(), Double.parseDouble(txtSalary.getText()));
        return em;
    }

    // tạo hàm addEmployee để thêm nhân viên vào table
    public void addEmployee() {
        if (validateFrom()) {
            // index = -1 thì sẽ thêm nới object NhanVien, ngược lại thì cập nhật NhanVien
            if (index == -1) {
                emService.addEmployee(readForm());
                fillTable();
                JOptionPane.showMessageDialog(this, "Add Employee Succesfully!");
            } else {
                // Khi  muốn cập nhật ta phải bt đâu là đối tượng chúng ta cần cập nhật 
                // Cập nhật theo mã NV hoặc theo index
                //   UpdateEmployee(readForm()) đưa tất cả các dữ liệu lên form
                int id = tblNhanVien.getSelectedRow();
                UpdateEmployee(readForm());
                fillTable();
                JOptionPane.showMessageDialog(this, "Update Successfully");
            }
        }
    }

    // Code hàm trả về Object NhanVien dựa trên mã Nv
    public Employee searchID(String ID) {
        for (Employee em : list) {
            if (em.getMaNV().equalsIgnoreCase(ID)) {
                return em;
            }
        }

        return null;
    }

    // Xây dựng hàm cập nhật , tạo đối tượng NhanVien mới đè lên đối tượng NhanVien cũ
    public void UpdateEmployee(Employee newNv) {
        if (validateFrom()) {
            // Nếu không tìm thấy đối tượng chúng ta muốn cập nhật thì phải Validate để tránh Exception 
            Employee emp1 = searchID(newNv.getMaNV());

            if (emp1 != null) {
                emp1.setHoTen(newNv.getHoTen());
                emp1.setAge(newNv.getAge());
                emp1.setEmail(newNv.getEmail());
                emp1.setLuong(newNv.getLuong());

            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Nhân Viên cần Update");
            }
        }
    }

    // Tạo hàm đưa thông tin từ table lên form để update theo id
    public void fillEmployeeUpForm(int index) {
        txtMaNV.setText(list.get(index).getMaNV());
        txtName.setText(list.get(index).getHoTen());
        txtEmail.setText(list.get(index).getEmail());
        txtAge.setText(String.valueOf((list.get(index).getAge())));
        txtSalary.setText(String.valueOf(list.get(index).getLuong()));
    }

    // TODO add your handling code here:
    // Tạo hàm đưa thông tin từ table lên form để update theo đối tượng
    public void fillEmployeeUpForm(Employee emp) {
        txtMaNV.setText(emp.getMaNV());
        txtName.setText(emp.getHoTen());
        txtEmail.setText(emp.getEmail());
        txtAge.setText(String.valueOf((emp.getAge())));
        txtSalary.setText(String.valueOf(emp.getLuong()));
    }

    // Tạo hàm xóa nhân viên
    public void deleteEmployee() {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên muốn xóa");
            return;
        } else {
            if (txtMaNV.getText().trim().equals("")) {
                JOptionPane.showConfirmDialog(this, "Nhập vào mã nhân viên muốn xóa");
                return;
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Delete", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    emService.deleteEmployee(index);
                    fillTable();
                    resetForm();
                    JOptionPane.showMessageDialog(this, "Delete Employeee Successfully!");
                }

            }
        }

    }

    // Hàm recode lấy thông tin bảng ghi
    public String getInfomation() {
        return "Record: " + (index + 1) + " of " + list.size();
    }

    public void updateInfor() {
        // setRowSelectionInterval là lựa chọn hàng trên cái bảng 
        tblNhanVien.setRowSelectionInterval(index, index);
        fillEmployeeUpForm(index);
        textBanGhi.setText(getInfomation());
    }

    // Tạo hàm First 
    public void firstEmployee() {
        if (list.size() != 0) {
            index = 0;
            updateInfor();
        }
    }

    public void lastEmployee() {
        if (list.size() != 0) {
            // Trong mảng bắt đầu từ 0 khi mà dùng hàm size() để lấy ra thông tin thì thông tin bắt đầu từ 1 
            if (list.size() != 0) {
                index = list.size() - 1;
                updateInfor();
            }
        }
    }

    public void preEmployee() {
        if (list.size() != 0) {
            if (index == 0) {
                lastEmployee();
            } else {
                index--;
                updateInfor();
            }
        }
    }

    public void nextEmployee() {
        if (index == (list.size() - 1)) {
            firstEmployee();
        } else {
            index++;
            updateInfor();
        }
    }

    // Xây dựng hàm validate form
    public boolean validateFrom() {
        if (txtMaNV.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã Nhân viên");
            return false;
        }
        for (Employee em : list) {
            if (em.getMaNV().equalsIgnoreCase(txtMaNV.getText())) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại vui lòng nhập mã mới");
                return false;
            }

        }

        if (txtName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên Nhân viên");
            return false;
        }
        if (txtAge.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tuổi Nhân viên");
            return false;
        }

        try {
            int age = Integer.parseInt(txtAge.getText());
            if (age < 16 || age > 55) {
                JOptionPane.showMessageDialog(this, "Tuổi phải từ 16 đến 55");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là số", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtEmail.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email");
            return false;
        }
        Matcher matcher = Pattern.compile(P_EMAIL).matcher(txtEmail.getText());
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Email sai định dạng", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtSalary.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lương của Nhân viên");
            return false;
        }
        try {
            Double luong = Double.parseDouble(txtSalary.getText());
            if (luong < 50000) {
                JOptionPane.showMessageDialog(this, "Lương phải trên 5 triệu");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải phải là số", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Hàm đọc file()
    public void readFile() {
        File file = new File("Listnv.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fis);
            ArrayList<Employee> ds = (ArrayList<Employee>) oos.readObject();
            emService.setList(ds);
            fillTable();
            textBanGhi.setText(getInfomation());
            oos.close();
            JOptionPane.showMessageDialog(this, "Read file successfully");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Hàm lưu file 
    public void saveFile() {
        File file = new File("Listnv.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            ArrayList<Employee> ds = emService.select();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ds);
            oos.close();
            JOptionPane.showMessageDialog(this, "Save file successfully");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtAge = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSalary = new javax.swing.JTextField();
        btnFirst = new javax.swing.JButton();
        btnPre = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnReadFile = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        textBanGhi = new javax.swing.JLabel();
        tblDongHo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");

        jLabel2.setText("MÃ NHÂN VIÊN");

        jLabel3.setText("HỌ VÀ TÊN");

        jLabel4.setText("TUỔI");

        jLabel5.setText("EMAIL");

        jLabel6.setText("LƯƠNG");

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPre.setText("<<");
        btnPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreActionPerformed(evt);
            }
        });

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnReadFile.setText("Open");
        btnReadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadFileActionPerformed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        textBanGhi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        textBanGhi.setForeground(new java.awt.Color(204, 0, 51));
        textBanGhi.setText("Record: 1 of 10");

        tblDongHo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblDongHo.setForeground(new java.awt.Color(255, 0, 51));
        tblDongHo.setText("00:00 Am");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnPre, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(textBanGhi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnReadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(tblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(14, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(tblDongHo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFind)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReadFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFirst)
                    .addComponent(btnPre)
                    .addComponent(btnNext)
                    .addComponent(btnLast)
                    .addComponent(textBanGhi))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Nút resetForm
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        resetForm();
    }//GEN-LAST:event_btnNewActionPerformed
    // Nút thêm Nhân viên 
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        addEmployee();
    }//GEN-LAST:event_btnSaveActionPerformed
    // Nút click vào 1 dòng của table sẽ hiển thị dữ liệu lên form
    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        index = tblNhanVien.getSelectedRow();
        fillEmployeeUpForm(index);
        textBanGhi.setText(getInfomation());
    }//GEN-LAST:event_tblNhanVienMouseClicked
    // Nút xóa Employee
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteEmployee();
    }//GEN-LAST:event_btnDeleteActionPerformed
    // Nút lưu file và thoát chương trình
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        saveFile();
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        if (searchID(txtMaNV.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Nhân Viên muốn tìm kiếm");
        } else {
            fillEmployeeUpForm(searchID(txtMaNV.getText()));
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        textBanGhi.setText(getInfomation());
    }//GEN-LAST:event_formWindowOpened

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        firstEmployee();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
        preEmployee();
    }//GEN-LAST:event_btnPreActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        nextEmployee();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        lastEmployee();

    }//GEN-LAST:event_btnLastActionPerformed
    // Nút đọc file
    private void btnReadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadFileActionPerformed
        readFile();
    }//GEN-LAST:event_btnReadFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPre;
    private javax.swing.JButton btnReadFile;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel tblDongHo;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JLabel textBanGhi;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtSalary;
    // End of variables declaration//GEN-END:variables
}
