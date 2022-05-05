/*
 * Created by JFormDesigner on Thu Apr 28 21:43:42 CST 2022
 */

package Frontdesk_Interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import Connection.ConnectionHandler;
import serverSocket.*;
import Connection.*;


/**
 * @author 1
 */
public class Frontdesk_showMyCode extends JFrame {
    private static int resultNum1 = 0;
    private static int resultNum2 = 0;
    private static boolean flag = true;
    static final Object lock = new Object();

    static class MyThread extends Thread {
        public void run() {

            synchronized (lock) {
                Test.openSocket();
            }
        }
    }

    public Frontdesk_showMyCode() {
        new MyThread().start();
        initComponents();

//方法1：开启端口监听客户支付状态


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
        button1 = new JButton();
        button2 = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();

        // System.out.println("当前路径="+this.getClass().getResource("/"));
//        img = new ImageIcon("src/main/java/img/new.jpg");// 创建图片对象
        try {
            img = new ImageIcon(ImageIO.read(new File("src/main/java/img/new.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        label2.setIcon(img);

        //======== this ========
        //setIconImage(new ImageIcon(getClass().getResource("src/main/java/img/new.jpg")).getImage());
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(800, 600));


        //---- label2 ----
        label2.setText("text");
        contentPane.add(label2);
        label2.setBounds(235, 100, 300, 300);

        //---- button1 -返回按钮---
        button1.setText("\u8fd4\u56de");
        contentPane.add(button1);
        button1.setBounds(0, 0, 65, button1.getPreferredSize().height);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                //跳转回选择结算方式界面
                new Frontdesk_settleMent();
            }
        });

        //---- button2 -结算完成按钮---
        button2.setText("\u7ED3\u7B97\u5B8C\u6210");
        contentPane.add(button2);
        button2.setBounds(700, 550, 100, 50);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    Connection conn = ConnectionHandler.getConnection();
                    Statement stmt = conn.createStatement();
                    String sql = "delete from shoppingCar";
                    stmt.executeUpdate(sql);
                }catch (SQLException r){
                    r.printStackTrace();
                }
                dispose();
            }
        });

        //---- label1 -请扫码支付---
        label1.setText("\u8bf7\u626b\u7801\u652f\u4ed8");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(300, 10), new Dimension(300, 50)));
        label1.setFont(new
                Font("STHeiti Light", Font.BOLD,
                30));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton button1;
    private JButton button2;
    private JLabel label1;
    private JLabel label2;
    private ImageIcon img;

    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        new Frontdesk_showMyCode();
    }

//    //内部类 : 线程，每三秒查询一次订单表数据，查看订单总数是否多了一单
//    class Mythread implements Runnable{
//
//        @Override
//        public void run() {
//            //三秒查询一次数据库订单表，以此查看用户是否支付成功
//            while (flag){
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                resultNum2 = new CountOrders().getOrdersNum();
//
//                if (resultNum2==(resultNum1+1)){
//                    //进入此分支，说明订单表新插入了一条订单，用户支付成功
//                    //设置flag退出while循环，完成线程任务
//                    flag=false;
//                    new Frontdesk_successPay();
//                    dispose();
//                }
//                 // System.out.println("resultNum1="+Frontdesk_showMyCode.resultNum1);
//                 // System.out.println("resultNum2="+Frontdesk_showMyCode.resultNum2);
//            }
//        }
//    }
//
//}

    //该类中有一个方法，getTotal_Orders 去获当前数据库订单表中的数据
    class CountOrders {
        Connection conn = null;
        int resultNum = 0;

        int getOrdersNum() {

            Connection conn = null;
            try {
                conn = ConnectionHandler.getConnection();
                String sql = "select count(*)  num from orders where order_time>date_sub(now(), interval 5 second)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    resultNum = rs.getInt("num");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //System.out.println("resultNum="+resultNum);
            return resultNum;
        }
    }
}


/*
try {
               Thread.sleep(3000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           Connection conn=null;
           try{
               conn = ConnectionHandler.getConnection();
               String sql = "select count(*)  num from orders where order_time>date_sub(now(), interval 5 second)";
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery();
               while(rs.next()){
                   resultNum = rs.getInt("num");
               }
           }catch (SQLException e){
               e.printStackTrace();
           }
 */

