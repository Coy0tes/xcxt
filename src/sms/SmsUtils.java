package sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SmsUtils {
public static void main(String[] args) throws Exception {
	
		String content = "【有机汇】您好，您的验证码是：123456"; 
		//String content = "【有机汇】您好，订单号为AAA的商品已发货。快递单号：BBB。如有疑问，请咨询400-007-0011。";
		//String content = "【有机汇】您好，您本次提现200元已经汇款到指定账号，请注意查收。如有疑问，请咨询400-007-0011。";
		//String content = "【有机汇】您好，订单号为AAAA的订单已取消。如有疑问，请咨询400-007-0011。";
		
		// 用户名
		String name="18653601113"; 
		// 密码
		String pwd="D9AF34C60E9E2398B9C1737080D7"; 
		// 电话号码字符串，中间用英文逗号间隔
		StringBuffer mobileString=new StringBuffer("15610273932");
		// 内容字符串
		StringBuffer contextString=new StringBuffer(content);
		
        System.out.println(doPost(mobileString, contextString)); //0,2017091117520674828903538,0,1,0,提交成功
    }
	
	/**
	 * 发送短信
	 * 
	 * @param name			用户名
	 * @param pwd			密码
	 * @param mobileString	电话号码字符串，中间用英文逗号间隔
	 * @param contextString	内容字符串
	 * @param sign			签名
	 * @param stime			追加发送时间，可为空，为空为及时发送
	 * @param extno			扩展码，必须为数字 可为空
	 * @return				
	 * @throws Exception
	 */
    public static String doPost(StringBuffer mobileString, StringBuffer contextString) throws Exception {
    	StringBuffer param = new StringBuffer();
    	param.append("name=18653601113");
    	param.append("&pwd=D9AF34C60E9E2398B9C1737080D7");
    	param.append("&mobile=").append(mobileString);
    	param.append("&content=").append(URLEncoder.encode(contextString.toString(),"UTF-8"));
    	param.append("&stime=");
    	param.append("&sign=");
    	param.append("&type=pt");
    	param.append("&extno=");
        
        URL localURL = new URL("http://web.cr6868.com/asmx/smsservice.aspx");
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String resultBuffer = "";
        
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            
            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            resultBuffer = convertStreamToString(inputStream);
            
        } finally {
            
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer;
    }
	
	
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               e.printStackTrace();    
            }    
        }    
        return sb1.toString();    
    }
}
