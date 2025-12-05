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

class sam2 extends JFrame implements ActionListener
{
    private Container c;
    private JButton delete;
     private JButton Back;
    private JLabel title;
    private JLabel empnum;
    private JTextField empno;
    public sam2(){
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
        
        Back = new JButton("Back");
        Back.setFont(new Font("Arial", Font.PLAIN, 15));
        Back.setSize(100, 20);
        Back.setLocation(400, 450);
        Back.addActionListener(this);
        c.add(Back);
        
        delete = new JButton("delete");
        delete.setFont(new Font("Arial", Font.PLAIN, 15));
        delete.setSize(100, 20);
        delete.setLocation(200, 450);
        delete.addActionListener(this);
        c.add(delete);
        
        setVisible(true);
         setVisible(true);
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==delete){
           
             String data3 = empno.getText();
                int empno=Integer.parseInt(data3);
                 
      try {
             String dbURL = "jdbc:mysql://localhost/swing"; // Replace with your database name
       
          Connection con = DriverManager.getConnection(dbURL,"root","root");
      Statement st = con.createStatement();
   
         String query = " delete from swing1 where empnum=?";
     

      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
     preparedStmt.setInt(1,empno);
   

      // execute the preparedstatement
      preparedStmt.execute();
     
      con.close();
      } catch (Exception e1) {
         e1.printStackTrace();
      }
    
     }
        else if(e.getSource()==Back){
            dispose();
            new sam1();
        }
        }
    }
    

