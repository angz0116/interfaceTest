package api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import bean.ApiDataBean;
import configs.ApiConfig;
import exceptions.ErrorRespStatusException;
import httputils.SSLClient;
import runfail.RetryListener;
import runfail.TestNGListener;
import utils.BaseUtils;
import utils.DecodeUtil;
import utils.RandomUtil;
import utils.ReportUtil;
import utils.StringUtil;


@Listeners({TestNGListener.class, RetryListener.class })
public class ApiTest extends TestBase {

	/**
	 * api请求跟路径
	 */
	private static String rootUrl;

	/**
	 * 跟路径是否以‘/’结尾
	 */
	private static boolean rootUrlEndWithSlash = false;

	/**
	 * 所有公共header，会在发送请求的时候添加到http header上
	 */
	private static Header[] publicHeaders;

	private static ApiConfig apiConfig;
	/**
	 * 所有api测试用例数据
	 */
	protected List<ApiDataBean> dataList = new ArrayList<ApiDataBean>();

	/**
	 * 初始化测试数据
	 * 
	 * @throws ErrorRespStatusException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@BeforeSuite
	public void init(ITestContext context) throws UnsupportedEncodingException,
			ClientProtocolException, IOException, ErrorRespStatusException,
			Exception {
		String configFilePath = Paths.get(System.getProperty("user.dir") , "api-config.xml").toString();
		ReportUtil.log("api config path:"+configFilePath);
		apiConfig = new ApiConfig(configFilePath);
		// 获取基础数据
		rootUrl = apiConfig.getRootUrl();
		rootUrlEndWithSlash = rootUrl.endsWith("/");

		// 读取 param，并将值保存到公共数据map
		Map<String, String> params = apiConfig.getParams();
		setSaveDates(params);

		List<Header> headers = new ArrayList<Header>();
		apiConfig.getHeaders().forEach((key,value)->{
			Header header = new BasicHeader(key,value);
			headers.add(header);
		});
		publicHeaders = headers.toArray(new Header[headers.size()]);

	}
	/**
	 * 过滤数据，run标记为Y的执行。
	 * 
	 * @return
	 * @throws DocumentException 
	 */
	@DataProvider(name = "apiDatas")
	public Iterator<Object[]> getApiData(ITestContext context) throws DocumentException {
		String excelPaths = context.getCurrentXmlTest().getParameter("excelPath");
		String sheetNames = context.getCurrentXmlTest().getParameter("sheetName");
		System.out.println(excelPaths+":"+sheetNames);
		dataList = readExcelData(ApiDataBean.class,excelPaths.split(";"), sheetNames.split(";"));
		List<Object[]> dataProvider = new ArrayList<Object[]>();
		for (ApiDataBean data : dataList) {
			if (data.isRun()) {
				dataProvider.add(new Object[] { data });
			}
		}
		return dataProvider.iterator();
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Test(dataProvider = "apiDatas")
	public void apiTest(ApiDataBean apiDataBean) throws Exception {
		String apiParam = buildRequestParam(apiDataBean);
		
		// 封装请求方法
		HttpUriRequest method = parseHttpRequest(apiDataBean.getUrl(),
				apiDataBean.getMethod(), apiParam);
		HttpClient client = new SSLClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); // 请求超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000); // 读取超时

		// 执行
		HttpResponse response = client.execute(method);
		int reponseStatus = response.getStatusLine().getStatusCode();
		if (StringUtil.isNotEmpty(apiDataBean.getStatus())) {
			Assert.assertEquals(reponseStatus, apiDataBean.getStatus(),"返回状态码与预期不符合!");
		} else {
			// 非2开头状态码为异常请求，抛异常后会进行重跑
			if (200 > reponseStatus || reponseStatus >= 300) {
				throw new ErrorRespStatusException("返回状态码异常：" + reponseStatus);
			}
		}
		HttpEntity respEntity = response.getEntity();
		String responseData;
		Header respContenType = response.getFirstHeader("Content-Type");
		if (respContenType != null && respContenType.getValue().contains("download")) {
			String conDisposition = response.getFirstHeader("Content-disposition").getValue();
			String fileType = conDisposition.substring(
					conDisposition.lastIndexOf("."), conDisposition.length());
			String filePath = "download/" + RandomUtil.getRandom(8, false)
					+ fileType;
			InputStream is = response.getEntity().getContent();
			Assert.assertTrue(BaseUtils.writeFile(is, filePath), "下载文件失败。");
			// 将下载文件的路径放到{"filePath":"xxxxx"}进行返回
			responseData = "{\"filePath\":\"" + filePath + "\"}";
		} else {
			responseData = DecodeUtil.decodeUnicode(EntityUtils.toString(respEntity));
		}

		// 输出返回数据log
		ReportUtil.log("resp:" + responseData);
		// 验证预期信息
		verifyResult(responseData, apiDataBean.getVerify(),
				apiDataBean.isContains());

		// 对返回结果进行提取保存。
		saveResult(responseData, apiDataBean.getSave());

	}

	private String buildRequestParam(ApiDataBean apiDataBean) {
		// 分析处理预参数 （函数生成的参数）
		String preParam = buildParam(apiDataBean.getPreParam());
		savePreParam(preParam);// 保存预存参数 用于后面接口参数中使用和接口返回验证中
		// 处理参数
		String apiParam = buildParam(apiDataBean.getParam());
		return apiParam;
	}

	/**
	 * 封装请求方法
	 * 
	 * @param url
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param param
	 *            请求参数
	 * @return 请求方法
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("deprecation")
	private HttpUriRequest parseHttpRequest(String url, String method,
			String param) throws UnsupportedEncodingException {
		// 处理url
		url = parseUrl(url);
		ReportUtil.log("method:" + method);
		ReportUtil.log("url:" + url);
		ReportUtil.log("param:" + param.replace("\r\n", "").replace("\n", ""));
		if ("post".equalsIgnoreCase(method)) {
			// 封装post方法
			HttpPost postMethod = new HttpPost(url);
			postMethod.setHeaders(publicHeaders);
			HttpEntity entity = new StringEntity(param, "UTF-8");
			postMethod.setEntity(entity);
			return postMethod;
		} else if ("upload".equalsIgnoreCase(method)) {
			HttpPost postMethod = new HttpPost(url);
			@SuppressWarnings("unchecked")
			Map<String, String> paramMap = JSON.parseObject(param, HashMap.class);
			MultipartEntity entity = new MultipartEntity();
			for (String key : paramMap.keySet()) {
				String value = paramMap.get(key);
				Matcher m = funPattern.matcher(value);
				if (m.matches() && m.group(1).equals("bodyfile")) {
					value = m.group(2);
					entity.addPart(key, new FileBody(new File(value)));
				} else {
					entity.addPart(key, new StringBody(paramMap.get(key)));
				}
			}
			postMethod.setEntity(entity);
			return postMethod;
		} else {
			// 封装get方法
			HttpGet getMethod = new HttpGet(url);
			getMethod.setHeaders(publicHeaders);
			return getMethod;
		}// delete put....
	}

	/**
	 * 格式化url,替换路径参数等。
	 * 
	 * @param shortUrl
	 * @return
	 */
	private String parseUrl(String shortUrl) {
		// 替换url中的参数
		shortUrl = getCommonParam(shortUrl);
		if (shortUrl.startsWith("http")) {
			return shortUrl;
		}
		if (rootUrlEndWithSlash == shortUrl.startsWith("/")) {
			if (rootUrlEndWithSlash) {
				shortUrl = shortUrl.replaceFirst("/", "");
			} else {
				shortUrl = "/" + shortUrl;
			}
		}
		return rootUrl + shortUrl;
	}
	 //获取返回的token ,使用JsonPath获取json路径
    public static HashMap<String,String> getToken(HttpResponse httpResponse,String jsonPath) throws Exception{
        HashMap<String,String> responseToken = new HashMap<String, String>();
        String responseString = EntityUtils.toString( httpResponse.getEntity(),"UTF-8");
        ReadContext ctx = JsonPath.parse(responseString);
        String Token = ctx.read(jsonPath); 
        if(null == Token||"".equals(Token)){
            new Exception("token不存在");
        }
        responseToken.put("x-ba-token",Token);
        return responseToken;
    }
    //获取状态码
    public static int getStatusCode(HttpResponse httpResponse){
        int StatusCode = httpResponse.getStatusLine().getStatusCode();
        return StatusCode;
    }

}
