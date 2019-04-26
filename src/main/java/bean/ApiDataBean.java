package bean;
/**
 * 
 * @author zhaoai
 *
 */
public class ApiDataBean extends BaseBean {
	private boolean run; // 是否已运行
	private String desc; // 接口描述
	private String url; // url地址
	private String method; //post，get请求
	private String param; // 请求的参数
	private boolean contains; // 比对字符串
	private String status; // 状态
	private String verify; // 返回的结果
	private String save; // 保存的返回数据
	private String preParam; // 与请求处理参数

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public boolean isContains() {
		return contains;
	}

	public void setContains(boolean contains) {
		this.contains = contains;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public String getPreParam() {
		return preParam;
	}

	public void setPreParam(String preParam) {
		this.preParam = preParam;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		 return String.format("desc:%s,method:%s,url:%s,param:%s",
				this.desc, this.method, this.url, this.param);
	}
	
	

}
