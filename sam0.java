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
class sam12 extends JFrame implements ActionListener{
    private Container c;
    private JLabel title;
    private JButton Admin;
    private JButton Student;
    public sam12()
    {
        setTitle("Registration Form");
     setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
 
        c = getContentPane();
        c.setLayout(null);
 
        title = new JLabel("Registration Form");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(300, 30);
        c.add(title);
        
        Admin = new JButton("Admin");
        Admin.setFont(new Font("Arial", Font.PLAIN, 15));
        Admin.setSize(100, 20);
        Admin.setLocation(300, 300);
        Admin.addActionListener(this);
        c.add(Admin);
        
        Student = new JButton("Employee");
        Student.setFont(new Font("Arial", Font.PLAIN, 15));
        Student.setSize(100, 20);
        Student.setLocation(450, 300);
        Student.addActionListener(this);
        c.add(Student);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e)
     {
         if(e.getSource()==Admin){
             dispose();
             new adminlogin();
             
         }
         if(e.getSource()==Student){
             dispose();
             new studentlogin();
         }
         
     }
    
     }

public class sam0  {
   public static void main(String aa[])
   {
       sam12 s=new sam12();
}
}