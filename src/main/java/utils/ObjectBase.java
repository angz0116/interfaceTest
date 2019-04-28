package utils;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import bean.Restful;
import configs.ApiConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
/**
 * 读取yaml文件
 * @author zhaoai
 *
 */

public class ObjectBase {
	/**
     * 读取不同类型的（如json,yaml）的模板
     * @param path
     * @param map
     * @return
     */
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(ObjectBase.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }
    /**
     * 修改api获取数据的bean
     * @param restful
     * @param map
     * @return
     */
    public Restful updateApiFromMap(Restful restful, HashMap<String, Object> map) {
        if(map==null){
            return  restful;
        }
        if (restful.method.toLowerCase().contains("get")) {
            map.entrySet().forEach(entry -> {
                restful.query.replace(entry.getKey(), entry.getValue().toString());
                System.out.println(restful.query);
            });
        }

        if (restful.method.toLowerCase().contains("post")) {
            if (map.containsKey("_body")) {
                restful.body = map.get("_body").toString();
            }
            if (map.containsKey("_file")) {
                String filePath = map.get("_file").toString();
                map.remove("_file");
                restful.body = template(filePath, map);
            }
        }
        return restful;

    }
    /**
     * 从yaml中读取数据
     * @param path
     * @return
     */
    public Restful getApiFromYaml(String path) {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(ApiConfig.class.getResourceAsStream(path), Restful.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
	public RequestSpecification getDefaultRequestSpecification() {
	        return given().log().all();
	
	}
	/**
	 * 从yaml中读取返回的数据
	 * @param path
	 * @param map
	 * @return
	 */
    public Response getResponseFromYaml(String path, HashMap<String, Object> map) {
        //fixed: 根据yaml生成接口定义并发送
        Restful restful = getApiFromYaml(path);
        restful = updateApiFromMap(restful, map);

        RequestSpecification requestSpecification = getDefaultRequestSpecification();

        if (restful.query != null) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }

        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }

        String[] url=updateUrl(restful.url);

        return requestSpecification.log().all()
                .header("Host", url[0])
                .when().request(restful.method, url[1])
                .then().log().all()
                .extract().response();

    }
    /**
     * 多环境支持，替换url，更新host的header
     * @param url
     * @return
     */
    private String[] updateUrl(String url) {
        HashMap<String, String> hosts=ApiConfig.getInstance().env.get(ApiConfig.getInstance().current);
        String host="";
        String urlNew="";
        for(Map.Entry<String, String> entry : hosts.entrySet()){
            if(url.contains(entry.getKey())){
                host=entry.getKey();
                urlNew=url.replace(entry.getKey(), entry.getValue());
            }
        }
        return new String[]{host, urlNew};
    }
	
}
