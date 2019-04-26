package httputils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * httpclient�������󵽷�����
 * @author admin
 *
 */
public class HttpClientUtil {
	 public static String getHttpContent(String url,String parameterData ) throws Exception {
         HttpURLConnection connection = null;
//       String content = "";
         OutputStream outputStream = null;
         OutputStreamWriter outputStreamWriter = null;
         InputStream inputStream = null;
         InputStreamReader inputStreamReader = null;
         BufferedReader reader = null;
         StringBuffer resultBuffer = new StringBuffer();
         String tempLine = null;
         try {
             URL address_url = new URL(url);
             connection = (HttpURLConnection) address_url.openConnection();
             connection.setRequestMethod("POST");
             connection.setDoOutput(true);
             connection.setDoInput(true);
             connection.setRequestProperty("accept", "*/*");
             connection.setRequestProperty("Accept-Charset", "utf-8");
             connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             connection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
             //���÷��ʳ�ʱʱ�估��ȡ��ҳ���ĳ���ʱ��,����ֵ
             System.setProperty("sun.net.client.defaultConnectTimeout","3000");
             System.setProperty("sun.net.client.defaultReadTimeout", "3000");
             outputStream = connection.getOutputStream();
             outputStreamWriter = new OutputStreamWriter(outputStream);

             outputStreamWriter.write(parameterData);
             outputStreamWriter.flush();

             if (connection.getResponseCode() >= 300) {
                 throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
             }
             inputStream = connection.getInputStream();
             inputStreamReader = new InputStreamReader(inputStream);
             reader = new BufferedReader(inputStreamReader);
             while ((tempLine = reader.readLine()) != null) {
                 resultBuffer.append(tempLine);
             }
         }finally {
             if(connection !=null){
                 connection.disconnect();
             }
         }
         return resultBuffer.toString();
     }
}
