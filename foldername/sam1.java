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
class sam1 extends JFrame implements ActionListener{
    private Container c;
    private JButton ins;
    private JButton delete;
    private JButton update;
    private JButton Back;
    public sam1(){
        c = getContentPane();
        c.setLayout(null);
        ins = new JButton("insert");
        ins.setFont(new Font("Arial", Font.PLAIN, 15));
        ins.setSize(100, 20);
        ins.setLocation(200, 450);
        ins.addActionListener(this);
        c.add(ins);
        delete = new JButton("delete");
        delete.setFont(new Font("Arial", Font.PLAIN, 15));
        delete.setSize(100, 20);
        delete.setLocation(600, 450);
        delete.addActionListener(this);
        c.add(delete);
        
        update = new JButton("update");
        update.setFont(new Font("Arial", Font.PLAIN, 15));
        update.setSize(100, 20);
        update.setLocation(400, 450);
        update.addActionListener(this);
        c.add(update);
        

        
        setVisible(true);
    }
     public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==ins){
            dispose();
            new sam();
        }
         if(e.getSource()==delete){
            dispose();
            new sam2();
        }
         if(e.getSource()==update){
            dispose();
            new sam3();
        }
         
         
    }
    }



