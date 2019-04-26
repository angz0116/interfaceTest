package appinterface;

import org.testng.annotations.Test;

import io.qameta.allure.Step;
import runfail.TestFailListener;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import java.util.Map;

import org.testng.annotations.AfterClass;



@Listeners({TestFailListener.class})
public class Test1 {
  public static final String PAGE_PATH = "";
  public static final String LOGIN_PAGE_NAME = "";
  
  @Test
  public void f() {
	 System.out.println("testng测试");
  }

	/*
	 * @Test(dataProvider ="testdata",description = "登录的反向测试用例") //@TargetPage(path=
	 * PAGE_PATH, pageName = LOGIN_PAGE_NAME)
	 * 
	 * @Step("方法faillogin步骤：输入错误的用户名：{0}/密码：{1}获取预期结果{2}") public void
	 * failLogin(Map<String ,String> testData) throws Exception {
	 * 
	 * }
	 */
  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

}
