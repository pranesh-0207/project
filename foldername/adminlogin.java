/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package swing2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import java.sql.*;
import java.util.*;
class adminlogin extends JFrame implements ActionListener {
    private Container c;
    private JLabel loginFrame;
    private JLabel UserId;
    private JTextField user;
    private JLabel Password;
    private JTextField pass;
    private JButton login;

    public adminlogin() {
        setTitle("Admin login");
     setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
 
        c = getContentPane();
        c.setLayout(null);
 
        loginFrame = new JLabel("Admin Login");
        loginFrame.setFont(new Font("Arial", Font.PLAIN, 30));
        loginFrame.setSize(300, 30);
        loginFrame.setLocation(300, 30);
        c.add(loginFrame);
        
        UserId = new JLabel("USER ID");
        UserId.setFont(new Font("Arial", Font.PLAIN, 20));
        UserId.setSize(200, 20);
        UserId.setLocation(100, 100);
        c.add(UserId);
 
        user = new JTextField();
        user.setFont(new Font("Arial", Font.PLAIN, 15));
        user.setSize(190, 20);
        user.setLocation(250, 100);
        c.add(user);
        
        Password = new JLabel("PASSWORD");
        Password.setFont(new Font("Arial", Font.PLAIN, 20));
        Password.setSize(200, 20);
        Password.setLocation(100, 150);
        c.add(Password);
 
        pass = new JTextField();
        pass.setFont(new Font("Arial", Font.PLAIN, 15));
        pass.setSize(150, 20);
        pass.setLocation(250, 150);
        c.add(pass);
        
        login = new JButton("Login");
        login.setFont(new Font("Arial", Font.PLAIN, 15));
        login.setSize(100, 20);
        login.setLocation(200, 200);
        login.addActionListener(this);
        c.add(login);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e)
     {
             if(e.getSource()==login){
                 if (user.getText().equals("admin") && pass.getText().equals("admin1")) {
                    dispose();  
                    new sam1(); 
                }

                /* else 
                 {
                     dispose();
                     new sam0();
                 }*/
             }
    
}
}