/*
 * Created by JFormDesigner on Fri Apr 29 13:49:05 CST 2022
 */

package Frontdesk_Interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import serverSocket.*;

import pay.WXPay;
import Frontdesk_Interface.Frontdesk_shoppingCar.*;

/**
 * @author 1
 */
public class Frontdesk_scanCode extends JFrame {
    private static int resultNum1=0;
    private static int resultNum2=0;
    private static boolean flag=true;

    public Frontdesk_scanCode() {
        initComponents();

//方法1：开启端口监听客户支付状态
//       Test.openSocket();


//方法2:三秒查询一次数据库订单表
 /*       //界面初始化完成，先进行一次当前数据库订单查询
        resultNum1 = new CountOrders().getOrdersNum();
        while (flag){
            //每三秒钟查询一次
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultNum2 = new CountOrders().getOrdersNum();
            if(resultNum2==(resultNum1+1)){
                //进入该分支说明订单表中新插入了一条数据
                flag=false;
            }
        }

        new Frontdesk_successPay();
        dispose();*/
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        textField1 = new JTextField();
        textField2 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(800,600));

        contentPane.add(textField1);
        contentPane.add(textField2);
        textField1.setBounds(210, 90, 220, textField1.getPreferredSize().height);
        textField2.setBounds(210, 125, 220, textField1.getPreferredSize().height);
        textField2.setText(String.valueOf(Frontdesk_shoppingCar.sumPrice));

        //---- button1 ----
        button1.setText("\u652f\u4ed8");
        contentPane.add(button1);
        button1.setBounds(155, 160, 75, button1.getPreferredSize().height);
        button1.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String num=textField1.getText();
                        try {
                            WXPay.scanCodeToPay(num);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );

        //---- button2 ----
        button2.setText("\u8fd4\u56de");
        contentPane.add(button2);
        button2.setBounds(0, 0, 75, button1.getPreferredSize().height);
        button2.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new Frontdesk_settleMent();
                    }
                }
        );

        //---- label1 ----
        label1.setText("\u6761\u5f62\u7801\u6570\u5b57\u4e32\uff1a");
        contentPane.add(label1);
        label1.setBounds(75, 95, 100, 20);

        label2.setText("\u652f\u4ed8\u91d1\u989d");
        contentPane.add(label2);
        label2.setBounds(75, 125, 100, 20);

        pack();
        this.setVisible(true);
        setLocationRelativeTo(getOwner());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField textField1;
    private JTextField textField2;
    private JButton button1;
    private JButton button2;
    private JLabel label1;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public static void main(String[] args) {
        new Frontdesk_scanCode();
    }
}
