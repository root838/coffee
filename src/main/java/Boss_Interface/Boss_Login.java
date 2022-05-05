/*
 * Created by JFormDesigner on Tue Apr 19 13:05:59 CST 2022
 */

package Boss_Interface;

import Connection.ConnectionHandler1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Main_Interface.*;

/**
 * @author 1
 */
public class Boss_Login extends JFrame {
    public Boss_Login() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        label2 = new JLabel();
        textField1 = new JTextField();
        passwordField = new JPasswordField();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        checkBox1 = new JCheckBox();

        //======== this ========
        final Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(800,600));

        //---- label1 用户名----
        label1.setText("\u7528\u6237\u540d");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(80, 70), label1.getPreferredSize()));

        //---- label2 密码----
        label2.setText("\u5bc6\u7801");
        contentPane.add(label2);
        label2.setBounds(85, 100, 35, 20);
        contentPane.add(textField1);
        textField1.setBounds(130, 70, 115, textField1.getPreferredSize().height);
        contentPane.add(passwordField);
        passwordField.setBounds(130, 100, 115, passwordField.getPreferredSize().height);

        //---- 显示密码按钮 ----
        checkBox1.setText("\u663e\u793a\u5bc6\u7801");
        contentPane.add(checkBox1);
        checkBox1.setBounds(new Rectangle(new Point(245, 97), checkBox1.getPreferredSize()));
        checkBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==ItemEvent.SELECTED){//被选中
                    passwordField.setEchoChar((char)0);
                }else{
                    passwordField.setEchoChar('*');
                }
            }
        });

        //---- button1 登录按钮----
        button1.setText("\u767b\u5f55");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(130, 140), button1.getPreferredSize()));
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText().trim();
                String password = passwordField.getText().trim();

                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(contentPane, "输入的用户名或密码不能为空！");
                    return;
                }


                //JDBC 查询当前用户用户名是否存在
                try {
                    Connection conn = ConnectionHandler1.getConnection();

                    String sql = "select * from boss where name = ? and password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, username);
                    pstmt.setString(2, password);

                    ResultSet rs = pstmt.executeQuery();

                     if (rs.next()) {
                        //若查询有结果，则说明用户已经存在
                        //JOptionPane.showMessageDialog(contentPane, "登陆成功！");
                         ConnectionHandler1.closeConnection();
                         pstmt.close();
                        dispose();
                        new Boss_Main();

                    }else{
                        JOptionPane.showMessageDialog(contentPane,"输入的用户名或密码错误！");
                    }


                }catch (Exception r){
                    r.printStackTrace();
                }
            }
        });

        //---- button2 返回按钮----
        button2.setText("\u8fd4\u56de");
        contentPane.add(button2);
        button2.setBounds(new Rectangle(new Point(0, 0), button2.getPreferredSize()));
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main_Interface();
            }
        });



        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JPasswordField passwordField;
    private JCheckBox checkBox1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        new Boss_Login();
    }
}
