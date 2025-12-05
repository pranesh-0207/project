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
public class studentlogin extends JFrame implements ActionListener{
    private Container c;
    private JLabel title;
    private JLabel empnum;
    private JTextField empno;
    private JButton Ok;
    public studentlogin() {
        setTitle("Admin login");
     setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
 
        c = getContentPane();
        c.setLayout(null);
 
        title = new JLabel("Employee Login");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(300, 30);
        c.add(title);
        
        empnum = new JLabel("empnum");
        empnum.setFont(new Font("Arial", Font.PLAIN, 20));
        empnum.setSize(100, 20);
        empnum.setLocation(100, 100);
        c.add(empnum);
 
        empno = new JTextField();
        empno.setFont(new Font("Arial", Font.PLAIN, 15));
        empno.setSize(190, 20);
        empno.setLocation(200, 100);
        
        c.add(empno);
        
        Ok = new JButton("OK");
        Ok.setFont(new Font("Arial", Font.PLAIN, 15));
        Ok.setSize(100, 20);
        Ok.setLocation(200, 200);
        Ok.addActionListener(this);
        c.add(Ok);
        setVisible(true);
    }  
 public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==Ok){
             String data3=empno.getText();
              int empno=Integer.parseInt(data3);
            dispose();
            new sam5(empno);
        }
    
}
}