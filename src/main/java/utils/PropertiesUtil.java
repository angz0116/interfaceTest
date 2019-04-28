package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Map.Entry;

public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
 
    private static Properties props;
    
    //代码块执行顺序：静态代码块>普通代码块>构造代码块
    //构造代码块每次都执行，但是静态代码块只执行一次
    static {

        String fileName = "/db.properties";

        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }
 
    //自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static String getProperty(String key){
        String value= props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }
 
    public static String getProperty(String key,String defaultValue){
        String value= props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
	public static Properties readProperties(File file) {
		try {
			return readProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return new Properties();
		}
	}

	public static Properties readProperties(InputStream inputStream) {
		Properties result = new Properties();
		if (inputStream != null) {
			try {
				result.load(inputStream);
			} catch (IOException ignored) {
			}
		}
		return result;
	}


	/*
	 * public String getProperties(String project_name,String filename,String key){
	 * new FileInputStream(System.getProperty("user.dir")+
	 * "/src/main/java/config/config.properties"); //获得当前项目的路径 String dir =
	 * BaseUtils.getProjectDir(); String desired_dir = dir +
	 * "src\\\\test\\\\resources" + "\\\\" + project_name + "\\\\" + filename;
	 * Properties prop = new Properties(); File f= new File(desired_dir); try{
	 * InputStream in = new FileInputStream(f); prop.load(in); param =
	 * prop.getProperty(key).trim(); }catch (IOException e){ e.printStackTrace(); }
	 * return param; }
	 * 
	 * public HashMap<String,String> propertiesToMap(String project_name,String
	 * fileName){ //获得当前项目的路径 String project_dir = BaseUtils.getProjectDir();
	 * System.out.println(project_dir); //String dir = project_dir + "test-classes"
	 * + "\\\\" + project_name + "\\\\" + fileName;
	 * 
	 * HashMap<String, String> map = null; InputStreamReader in = null; Properties
	 * prop = null; Set<Entry<Object,Object>> entrySet = null; try{ map = new
	 * HashMap<String,String>(); in = new InputStreamReader(new
	 * FileInputStream(project_dir)); prop = new Properties(); prop.load(in);
	 * entrySet = prop.entrySet(); }
	 * 
	 * catch(FileNotFoundException e){ e.printStackTrace(); } catch(IOException e){
	 * e.printStackTrace(); } if(entrySet!=null && entrySet.size()>0){
	 * for(Entry<Object, Object> entry : entrySet){
	 * if(!entry.getKey().toString().startsWith("#")){
	 * map.put(((String)(entry.getKey())).trim(),((String)(entry.getValue())).trim()
	 * ); } } } return map; }
	 */
}
