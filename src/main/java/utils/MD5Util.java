package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * 1.MD5加密字符串（32位大写）
 * 2.MD5加密字符串（32位小写）
 * <p>
 * MD5在线加密：https://md5jiami.51240.com/
 * 3.将二进制字节数组转换为十六进制字符串
 * 4.Unicode中文编码转换成字符串
 */
public class MD5Util {
 
    /**
     * MD5加密字符串（32位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（大写）
     */
    public static String md5Encrypt32Upper(String string) {
        byte[] hash;
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
 
        //转换为十六进制字符串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }
 
    /**
     * MD5加密字符串（32位小写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（小写）
     */
    public static String md5Encrypt32Lower(String string) {
        byte[] hash;
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
 
        //转换为十六进制字符串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toLowerCase();
    }
 
    /**
     * 将二进制字节数组转换为十六进制字符串
     *
     * @param bytes 二进制字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
 
    /**
     * Unicode中文编码转换成字符串
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 
     * @param path  请求路径，域名和问号之间的部分
     * @param appId
     * @param timeStamp
     * @param secretKey
     * @param params  所有的请求参数组成的map
     * @return
     */
    public static String buildSn(String path,String appId, String timeStamp, String secretKey, Map<String, String> params){
        Map<String,String> sortedMap = new TreeMap<String,String>(new Comparator<String>(){
            public int compare(String arg0, String arg1) {
                return arg0.compareTo(arg1);
            }
        });
        sortedMap.put("path", path);
        sortedMap.put("appid",appId);
        sortedMap.put("timestamp", timeStamp);
        sortedMap.putAll(params);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            sb.append("&");
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        sb.append("&secretkey="+secretKey);
        String nomalSn = sb.toString().substring(1);
        return MD5Util.md5Encrypt32Lower(nomalSn);
    }
    public static void main(String [] args) {
    	Map<String, String> params = new HashMap<String,String>();

    	System.out.println(buildSn("/userprofile/headInfo","kj","1555915558","11111",params));
    }

}
