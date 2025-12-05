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
/**
 *
 * @author ADMIN
 */
class sam extends JFrame implements ActionListener
{
    private Container c;
    private JLabel title;
    private JLabel empnum;
    private JTextField empno;
    private JLabel empname;
    private JTextField empnam;
    private JLabel email;
    private JTextField emailid;
    private JLabel empposition;
    private JTextField empposi;
    private JLabel empsalary;
    private JTextField empsala;
    private JButton ins;
    private JButton clear;
    private JButton delete;
    private JButton Back;
    public sam()
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
 
        empname = new JLabel("empname");
        empname.setFont(new Font("Arial", Font.PLAIN, 20));
        empname.setSize(100, 20);
        empname.setLocation(100, 150);
        c.add(empname);
 
        empnam = new JTextField();
        empnam.setFont(new Font("Arial", Font.PLAIN, 15));
        empnam.setSize(150, 20);
        empnam.setLocation(200, 150);
        c.add(empnam);
       
        email = new JLabel("email");
        email.setFont(new Font("Arial", Font.PLAIN, 20));
        email.setSize(100, 20);
        email.setLocation(100, 200);
        c.add(email);
 
        emailid = new JTextField();
        emailid.setFont(new Font("Arial", Font.PLAIN, 15));
        emailid.setSize(150, 20);
        emailid.setLocation(200, 200);
        c.add(emailid);
       
        empposition = new JLabel("empposition");
        empposition.setFont(new Font("Arial", Font.PLAIN, 20));
        empposition.setSize(100, 20);
        empposition.setLocation(100, 250);
        c.add(empposition);
 
        empposi = new JTextField();
        empposi.setFont(new Font("Arial", Font.PLAIN, 15));
        empposi.setSize(150, 20);
        empposi.setLocation(200, 250);
        c.add(empposi);
        empsalary = new JLabel("empsalary");
        empsalary.setFont(new Font("Arial", Font.PLAIN, 20));
        empsalary.setSize(100, 20);
        empsalary.setLocation(100, 300);
        c.add(empsalary);
 
        empsala = new JTextField();
        empsala.setFont(new Font("Arial", Font.PLAIN, 15));
        empsala.setSize(150, 20);
        empsala.setLocation(200, 300);
        c.add(empsala);
       
        ins = new JButton("insert");
        ins.setFont(new Font("Arial", Font.PLAIN, 15));
        ins.setSize(100, 20);
        ins.setLocation(200, 450);
        ins.addActionListener(this);
        c.add(ins);
        
        clear = new JButton("clear");
        clear.setFont(new Font("Arial", Font.PLAIN, 15));
        clear.setSize(100, 20);
        clear.setLocation(400, 450);
        clear.addActionListener(this);
        c.add(clear);
        
         Back = new JButton("Back");
        Back.setFont(new Font("Arial", Font.PLAIN, 15));
        Back.setSize(100, 20);
        Back.setLocation(600, 450);
        Back.addActionListener(this);
        c.add(Back);
        setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent e)
     {
             if(e.getSource()==ins){
                 String data3=empno.getText();
                 int empno=Integer.parseInt(data3);
                 String data4=empnam.getText();
                String data5=emailid.getText();
                String data6=empposi.getText();
                String data7=empsala.getText();
                int empsala=Integer.parseInt(data7);
     
      String myUrl = "jdbc:mysql://localhost/swing";
     
        // create a sql date object so we can use it in our INSERT statement
        try (Connection conn = DriverManager.getConnection(myUrl, "root", "root")) {
            // create a sql date object so we can use it in our INSERT statement
   

            // the mysql insert statement
            String query = "insert into swing1(empnum,empname,email,empposition,empsalary)"+ "values(?,?,?,?,?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, empno);
            
             preparedStmt.setString (2,data4);
             preparedStmt.setString (3,data5);
             preparedStmt.setString (4,data6);
             preparedStmt.setInt(5,empsala);
             
             
            

            // execute the preparedstatement
            preparedStmt.execute();
        }catch(Exception e8)
    {
      System.out.println("Got an exception!");
      System.out.println(e8);
    }
             }
             else if(e.getSource()==clear)
      {
       
            empno.setText("");
            empnam.setText("");
            emailid.setText("");
                empposi.setText("");
                empsala.setText("");

           
                    }
            else if(e.getSource()==Back){
            dispose();
            new sam1();
        }
             
             
             
             
}  
}
