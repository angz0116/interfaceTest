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
import java.util.Map.Entry;

public class PropertiesUtil {


	private String param;
	public String getProperties(String project_name,String filename,String key){
		/*new FileInputStream(System.getProperty("user.dir")+"/src/main/java/config/config.properties");*/
		//获得当前项目的路径
		String dir = BaseUtils.getProjectDir();
		String desired_dir = dir + "src\\\\test\\\\resources" + "\\\\" + project_name + "\\\\" + filename;
	    Properties prop = new Properties();
	    File f= new File(desired_dir);
	    try{
	    	InputStream in = new FileInputStream(f);
	    	prop.load(in);
	    	param = prop.getProperty(key).trim();	    	
	    }catch (IOException e){
	    	e.printStackTrace();
	   }
		return param;
	}
	
	public HashMap<String,String> propertiesToMap(String project_name,String fileName){
		//获得当前项目的路径
		String project_dir = BaseUtils.getProjectDir();
		System.out.println(project_dir);
		String dir = project_dir + "test-classes" + "\\\\" + project_name + "\\\\" + fileName;
		
		HashMap<String, String> map = null;
		InputStreamReader in = null;
		Properties prop = null;
		Set<Entry<Object,Object>> entrySet = null;
		try{
			map = new HashMap<String,String>();
			in = new InputStreamReader(new FileInputStream(dir));
			prop = new Properties();
			prop.load(in);
			entrySet = prop.entrySet();								
		}
		
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		if(entrySet!=null && entrySet.size()>0){
			for(Entry<Object, Object> entry : entrySet){
				if(!entry.getKey().toString().startsWith("#")){				
					map.put(((String)(entry.getKey())).trim(),((String)(entry.getValue())).trim());
				}
			}
		}
		return map;
	}
}
