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

class MyFrame5 extends JFrame implements ActionListener{
 
   
    // Components of the Form
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
   
    private JButton sub;
        private JButton update;
 
    private JButton delete;
    private JButton clear;
     public MyFrame5()
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
       
        sub = new JButton("insert");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(200, 450);
        sub.addActionListener(this);
        c.add(sub);
       
 update = new JButton("update");
        update.setFont(new Font("Arial", Font.PLAIN, 15));
        update.setSize(100, 20);
        update.setLocation(300, 450);
        update.addActionListener(this);
        c.add(update);
       
        clear = new JButton("clear");
        clear.setFont(new Font("Arial", Font.PLAIN, 15));
        clear.setSize(100, 20);
        clear.setLocation(400, 450);
        clear.addActionListener(this);
        c.add(clear);
   
        delete = new JButton("delete");
        delete.setFont(new Font("Arial", Font.PLAIN, 15));
        delete.setSize(100, 20);
        delete.setLocation(500, 450);
        delete.addActionListener(this);
        c.add(delete);
   
         setVisible(true);
    }
     
     public void actionPerformed(ActionEvent e)
     {
             if(e.getSource()==sub){
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
    else if(e.getSource()==update)
        {
            //JOptionPane.showMessageDialog(null,"jj","Info",JOptionPane.INFORMATION_MESSAGE);
             String data3 = empnam.getText();
               
                String data4=email.getText();
                String data5=empposition.getText();
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
         else if(e.getSource()==clear)
      {
       
            empno.setText(" ");
            empnam.setText(" ");
            emailid.setText(" ");
               empposi.setText(" ");
               empsala.setText(" ");
           
                    }
    else if(e.getSource()==delete)
        {
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


     }
public class Swing2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MyFrame5 v6=new MyFrame5();
                
} 
    }
   
}

 

