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
class sam4 extends JFrame  implements ActionListener{
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
    private JButton update;
   
    public sam4(int empno1)
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
        empno.setText(String.valueOf(empno1));
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
       
        update = new JButton("update");
        update.setFont(new Font("Arial", Font.PLAIN, 15));
        update.setSize(100, 20);
        update.setLocation(300, 450);
        update.addActionListener(this);
        c.add(update);
        setVisible(true);
       
           
           try{
                String my="jdbc:mysql://localhost/swing";
              Connection c=DriverManager.getConnection(my,"root","root");
           
                  
                   String query="select * from swing1 where empnum=?";
                   PreparedStatement stmt=c.prepareStatement(query);
                   stmt.setInt(1,empno1);
                   ResultSet rs = stmt.executeQuery(); 

                           
               if(rs.next()){
                   String m1=rs.getString("empname");
                   empnam.setText(m1);
                   String m2=rs.getString("email");
                   emailid.setText(m2);
                   String m3=rs.getString("empposition");
                   empposi.setText(m3);
                   int m4=rs.getInt("empsalary");
                   empsala.setText(String.valueOf(m4));
                  System.out.println("empname:"+rs.getString("empname"));

                 }
               
           }catch (Exception e1){
                       System.out.println(e1);
                       }
       }
      
    

     public void actionPerformed(ActionEvent e)
     {
         if(e.getSource()==update)
        {
            //JOptionPane.showMessageDialog(null,"jj","Info",JOptionPane.INFORMATION_MESSAGE);
             String data3 = empnam.getText();
               
                String data4 = emailid.getText();
                String data5 = empposi.getText();

                String data6=empsala.getText();
                int empsala=Integer.parseInt(data6);
                 String data7 = empno.getText();
                int empno=Integer.parseInt(data7);
                  
      
        String mtu = "jdbc:mysql://localhost/swing";
        try(Connection con = DriverManager.getConnection(mtu,"root","root")){
           
       
   
         String query ="update swing1 set empname=?,email=?,empposition=?,empsalary=? where empnum=?";
       
            Statement st = con.createStatement();
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString(1,data3);
      preparedStmt.setString(2,data4);
      preparedStmt.setString(3,data5);
      preparedStmt.setInt(4,empsala);
      preparedStmt.setInt(5,empno);
      preparedStmt.execute();
         
      }catch(Exception e6)
      {
          System.out.println(e6);
      }
        }
     }
     }



