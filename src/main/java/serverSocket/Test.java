package serverSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import Frontdesk_Interface.*;


public class Test {
    public static void openSocket() {
        int port=22051;
        ServerSocket ss=null;
        try{
            ss = new ServerSocket(port);
            System.out.println("成功开启端口监听！"+port+"...");
            Socket socket = ss.accept();
            InputStream is=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            String message=br.readLine();

            if(message!=null){
                new Frontdesk_successPay();
            }else {
                System.out.println("支付失败");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
