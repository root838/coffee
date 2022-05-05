package pay;

import Frontdesk_Interface.Frontdesk_info;
import Frontdesk_Interface.Frontdesk_shoppingCar;
import sdk.WXPayUtil;
import util.DateUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import Connection.ConnectionHandler;
import Frontdesk_Interface.Frontdesk_Login;

public class WXPay {

    private static Log log = LogFactory.getLog(WXPay.class);

    private static final String PAY_SUCCESS = "SUCCESS";
    private static final String PAY_USERPAYING = "USERPAYING";

    public static void main(String[] args) throws Exception {

        // 生成二维码，完成支付
         unifiedOrder();
        // 商家扫用户手机的条形码
        scanCodeToPay("");

    }

    /**
     * 扫码支付
     *
     * @throws Exception
     */

    //扫描用户付款码支付
    public static String scanCodeToPay(String auth_code) throws Exception {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String spbill_create_ip = addr.getHostAddress();

        MyConfig config = new MyConfig();
        sdk.WXPay wxpay = new sdk.WXPay(config);
        String out_trade_no = DateUtil.getCurrentTime();
        //支付金额，需要转成字符串类型，否则后面的签名会失败
        float a = Frontdesk_shoppingCar.sumPrice*100;
        int total_fee =(int)a;//100分：1块钱
        Map<String, String> map = new HashMap<String,String>(16);
        map.put("attach", "订单额外描述");
        map.put("auth_code", auth_code);
        map.put("body", "小米手机");
        map.put("device_info", "桂电1号店");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("out_trade_no", out_trade_no);
        map.put("spbill_create_ip", spbill_create_ip);
        map.put("total_fee", String.valueOf(total_fee));
        //生成签名
        String sign = WXPayUtil.generateSignature(map, config.getKey());
        map.put("sign", sign);
        String mapToXml = null;
        try {
            //调用微信的扫码支付接口
            Map<String, String> resp = wxpay.microPay(map);
            System.out.println("扫码支付结果：" + resp);
            mapToXml = WXPayUtil.mapToXml(resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信支付失败" + e);
        }
        return "";
    }
    static int id=0;
    static int num=0;
    static float price=0;
    static int user_id=0;
    //取数据
    public static void getdata(){
        Connection conn = null;
        Connection conn1 = null;

        Statement stmt = null;//SQL语句对象，拼SQL
        Statement stmt1 = null;
        String sql = "SELECT * FROM shoppingCar";
        String sql1 = "SELECT * FROM emp where empAccount='"+Frontdesk_Login.username+"'";
        //System.out.println("即将执行的sql：" + sql);
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            conn = ConnectionHandler.getConnection();
            conn1 = ConnectionHandler.getConnection();
            stmt = conn.createStatement();
            stmt1 = conn1.createStatement();
            rs = stmt.executeQuery(sql);
            rs1 = stmt1.executeQuery(sql1);
            while (rs.next()) {
                //每循环一次就是一个对象，把这个对象放入容器（List（有序可重复）、Set（无序不可重复）、Map（key、value结构）

                id=rs.getInt(1);
                num=rs.getInt(3);
                price=rs.getFloat(4);

            }
            while (rs1.next()) {
                user_id=rs1.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //释放资源：数据库连接很昂贵
            try {
                rs.close();
                stmt.close();
                conn.close();//关连接
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    下单：生成二维码
     */
    public static void unifiedOrder() {
        Map<String, String> resultMap = new HashMap();
        String openid = "ouR0E1oP5UGTEBce8jZ_sChfH26g";
        MyConfig config = null;
        sdk.WXPay wxpay = null;
        try {
            config = new MyConfig();
            wxpay = new sdk.WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        //获取本机的ip地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String spbill_create_ip = addr.getHostAddress();
        //支付金额，需要转成字符串类型，否则后面的签名会失败
        float a = Frontdesk_shoppingCar.sumPrice*100;
        int total_fee =(int)a;//100分：1块钱
        System.out.println(total_fee);
        //System.out.println("生成二维码，当前传入的金额为："+total_fee);

        //商品描述
        String body = "买咖啡钱！";
        //商户订单号
        String out_trade_no = WXPayUtil.generateNonceStr();
        //统一下单接口参数
        SortedMap<String, String> data = new TreeMap<String, String>();
        data.put("appid", "wxd9a46e74fc279fcc");
        data.put("body", body);
        data.put("mch_id", "1623889015");
        // 回调接口，必须是一个域名，不能使用IP
        // 腾讯会自动调用你（程序自己提供的接口）的接口，给你发送支付结果的数据，数据格式：xml格式
        data.put("notify_url", "http://52e281v047.zicp.vip/result");
        data.put("out_trade_no", out_trade_no);//交易号
        data.put("spbill_create_ip", spbill_create_ip);//下单的电脑IP地址
        data.put("trade_type", "NATIVE");//支付类型
        data.put("total_fee", String.valueOf(total_fee));
//        data.put("openid", openid);
        data.put("attach","item_id,"+id+";item_price,"+price+";user_id,"+user_id+";amount,"+num+";");

        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            createQRCode(rMap);//生成二维码
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //C:\Users\19793\Desktop\wxpaydemo\src\main\java\cn\juntai\wxpaydemo\img

    public static void createQRCode(Map<String, String> map) throws Exception {

        //获取当前项目的路径
        String path = System.getProperty("user.dir")+"/src/main/java/img/new.jpg";
        //由于Windows系统的问题，需要将获取到的路径中的 \ 转换为 \\
        //很奇怪，因为本身 \ 是敏感字符，需要对其进行转义，一个反斜杠 需要写 四个反斜杠 才能对其进行转义
        path = path.replaceAll("\\\\","\\\\\\\\");
        // System.out.println("path="+path);

        //在当前项目路径下创建 img包，把生成的二维码放到该包下面
        File outputFile = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        String url = map.get("code_url");
        // System.out.println("生成二维码的url：" + url);

        try {
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);

            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", fileOutputStream);
        } catch (Exception e) {
            throw new Exception("生成二维码失败！");
        } finally {
            fileOutputStream.close();
        }
    }
}