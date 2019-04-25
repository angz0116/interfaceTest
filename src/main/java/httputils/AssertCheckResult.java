package httputils;
import org.testng.Assert;
/**
 * 预期结果与实际结果的对比
 * @author admin
 *
 */
public class AssertCheckResult {
	 /*----------------------------------------断言--------------------------------------------------*/

    /**
     * 断言预期字符串与实际字符串相等
     *
     * @param expected
     * @param actural
     */
    public void assertTextEqual(String expected, String actural) {
        Assert.assertEquals(expected, actural);
    }

    /**
     * 断言实际文本包含预期文本
     *
     * @param expected
     * @param actural
     */
    public void assertTextContain(String expected, String actural) {
        try {
            Assert.assertTrue(actural.contains(expected));
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("断言失败！");
        }
    }

    /**
     * 断言非空
     *
     * @param actural
     */
    public void assertNotNull(String actural) {
        Assert.assertNotNull(actural);
    }

    public void assertTrue(Boolean flag){
        Assert.assertTrue(flag);
    }

}
