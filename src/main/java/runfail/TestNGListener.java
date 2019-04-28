package runfail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
/**
  *    测试案例testNg的listener(成功，失败，跳过，保存结果，是否完成)
 * @author zhaoai
 *
 */
public class TestNGListener extends TestListenerAdapter {

	@Override
	public void onTestSuccess(ITestResult tr) {
		// TODO Auto-generated method stub
		TestNGRetry.resetRetryCount();
		super.onTestSuccess(tr);
	}

	public void onTestFailure(ITestResult tr) {
		saveResult(tr);
		super.onTestFailure(tr);
	}

	public void onTestSkipped(ITestResult tr) {
		saveResult(tr);
		super.onTestSkipped(tr);
	}

	private void saveResult(ITestResult tr) {
		Throwable throwable = tr.getThrowable();
		if (null == throwable) {
			return;
		}
		 Reporter.setCurrentTestResult(tr);
		 Reporter.log("保存结果");
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);

		// List of test results which we will delete later
		ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
		// collect all id's from passed test
		Set<Integer> passedTestIds = new HashSet<Integer>();
		for (ITestResult passedTest : testContext.getPassedTests()
				.getAllResults()) {
			// logger.info("PassedTests = " + passedTest.getName());
			passedTestIds.add(getId(passedTest));
		}

		Set<Integer> failedTestIds = new HashSet<Integer>();
		for (ITestResult failedTest : testContext.getFailedTests()
				.getAllResults()) {
			// logger.info("failedTest = " + failedTest.getName());
			// id = class + method + dataprovider
			int failedTestId = getId(failedTest);

			if (failedTestIds.contains(failedTestId)|| passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);
			} else {
				failedTestIds.add(failedTestId);
			}
		}

		for (Iterator<ITestResult> iterator =

		testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext();) {
			ITestResult testResult = iterator.next();
			if (testsToBeRemoved.contains(testResult)) {
				// logger.info("Remove repeat Fail Test: " +
				// testResult.getName());
				iterator.remove();
			}
		}

	}

	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id+ (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}
}