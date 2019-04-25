package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class BaseUtils {
	public static ObjectBase pageobjectsbase = new ObjectBase();
	//获得当前项目的根目录
	public static String getProjectDir(){
		String dir = Class.class.getClass().getResource("/").getPath();	
		String desired_dir = "";
		List<String> list = new ArrayList<String>();
		String[] arr = dir.split("/");
		for(int i = 0; i < arr.length; i++){
			list.add(arr[i]);
		}
		list.remove(list.size()-1);
		list.remove(0);
		for(int i = 0; i < list.size(); i++){
			desired_dir = desired_dir + list.get(i) + "\\\\";
		}
		//替换路径中的“%20”为空格
		desired_dir = desired_dir.replaceAll("%20", " ");
		
		String os = System.getProperty("os.name");
		if(!os.toLowerCase().startsWith("win")){
			desired_dir = "/"+desired_dir;
			desired_dir = desired_dir.replaceAll("\\\\","/");
		}
		return desired_dir;
	}
	
	public static void deleteAllFilesInPath(String path){
		int i;
		try{			
			File folder = new File(path);
			File[] files = folder.listFiles();
			if(files!=null && files.length>0){
				for(i = 0;i<files.length;i++){
					if(files[i].isDirectory()){
						deleteAllFilesInPath(files[i].getAbsolutePath());//递归清空子文件夹
						files[i].delete();
						System.out.println("Delete directory successfully!");
					}else{
						files[i].delete();
					}
				}
			 }
			System.out.println("Delete all files successfully!");
		}catch(Exception e){
			System.out.println("Errors when delete files");
			e.printStackTrace();
		}
		
		
	}
	
	//Get absolute file path for a file which name contains text in dir
	public static String getFilePath(String directory,String text){
		File file = new File(directory);
		String filepath = null;
		File[] filelist = file.listFiles();
		if(filelist!=null && filelist.length>0){
			for(int i=0;i<filelist.length;i++){
				if(filelist[i].getName().contains(text)){
					filepath = filelist[i].getAbsolutePath();				
				}		
			}
		}
		return filepath;
	}
	
}
