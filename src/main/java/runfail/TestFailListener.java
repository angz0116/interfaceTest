package runfail;

import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * 
 * @author zhaoai
 *
 */
public class TestFailListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        takePhoto();
    }

    @Attachment(value = "screen shot",type = "image/png")
    public byte[]  takePhoto(){
        byte[] screenshotAs = {1,2};//((TakesScreenshot)BaseParpare.driver).getScreenshotAs(OutputType.BYTES);
        return screenshotAs;
    }
}
