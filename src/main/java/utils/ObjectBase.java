package utils;

import java.util.HashMap;

import utils.PropertiesUtil;

public class ObjectBase {
	private PropertiesUtil properties = new PropertiesUtil();
	//此处把properties文件读到Hashmap
	public HashMap<String,String> config = properties.propertiesToMap("config","config.properties");
	public HashMap<String,String> db = properties.propertiesToMap("config","db.properties");
	
	public static void main(String [] args) {
		ObjectBase obj = new ObjectBase();
		System.out.println(obj.db);
	}
}
