package httputils;
import org.testng.Assert;
/**
   *    预期结果与实际结果的对比
 * @author zhaoai
 *
 */
public class AssertCheckResult {
	 /*----------------------------------------断言--------------------------------------------------*/

    /**
             *    断言预期字符串与实际字符串相等
     *
     * @param expected
     * @param actural
     */
    public static void assertTextEqual(String expected, String actural) {
        Assert.assertEquals(expected, actural);
    }

    /**
             *    断言实际文本包含预期文本
     *
     * @param expected
     * @param actural
     */
    public static void assertTextContain(String expected, String actural) {
        try {
            Assert.assertTrue(actural.contains(expected));
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("断言失败！");
        }
    }

    /**
             *    断言非空
     *
     * @param actural
     */
    public static void assertNotNull(String actural) {
        Assert.assertNotNull(actural);
    }
    /**
             *    断言是否正确
     * @param flag
     */
    public static void assertTrue(Boolean flag){
        Assert.assertTrue(flag);
    }
    /**
             *    根据原输入项与搜索项比对，是否包含
     * @param source
     * @param search
     */
    public static void contains(String source, String search) {
		Assert.assertTrue(source.contains(search),
				String.format("期待'%s'包含'%s'，实际为不包含.", source, search));
	}
	/**
	   *    根据原输入项与搜索项比对，是否不包含
	 * @param source
	 * @param search
	 */
	public static void notContains(String source, String search) {
		Assert.assertFalse(source.contains(search),
				String.format("期待'%s'不包含'%s'，实际为包含.", source, search));
	}
}
