package configs;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.specification.RequestSpecification;

/**
 * Api配置信息
 * 
 * @author zhaoai
 *
 */
public class ApiConfig {
	
	private static ApiConfig apiConfig;

	public String current = "test";

	public HashMap<String, HashMap<String, String>> env;
	
	public HashMap<String, String> header;
	
	/*
	 * private String rootUrl;
	 * public String getRootUrl() {
	 *     return rootUrl;
	 * }
	 * private Map<String, String> headers = new HashMap<String, String>();
	 * public Map<String, String> getHeaders() {
		  return headers;
	   }
	   private Map<String, String> params = new HashMap<String, String>();
	   public Map<String, String> getParams() {
		return params;
	}
	 */

	/*
	 * public ApiConfig(String configFilePath) throws DocumentException{
	 * 
	 * SAXReader reader = new SAXReader(); Document document =
	 * reader.read(configFilePath); Element rootElement = document.getRootElement();
	 * 
	 * //rootUrl = rootElement.element("rootUrl").getTextTrim();
	 * 
	 * @SuppressWarnings("unchecked") List<Element> paramElements =
	 * rootElement.element("params").elements("param"); for (Element ele :
	 * paramElements) {
	 * params.put(ele.attributeValue("name").trim(),ele.attributeValue("value").trim
	 * ()); }
	 * 
	 * @SuppressWarnings("unchecked") List<Element> headerElements =
	 * rootElement.element("headers").elements("header");
	 * 
	 * for (Element ele : headerElements) {
	 * headers.put(ele.attributeValue("name").trim(),ele.attributeValue("value").
	 * trim()); }
	 * 
	 * headerElements.forEach((ele)->{
	 * headers.put(ele.attributeValue("name").trim(),
	 * ele.attributeValue("value").trim()); });
	 * 
	 * }
	 */
	public static ApiConfig getInstance() {
		if (apiConfig == null) {
			apiConfig = load("/config/config.yaml");
			System.out.println(apiConfig.header);
		}
		return apiConfig;
	}

	public static ApiConfig load(String path) {
		// fixed: read from yaml or json
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			return mapper.readValue(ApiConfig.class.getResourceAsStream(path), ApiConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
