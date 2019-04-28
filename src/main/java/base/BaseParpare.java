package base;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.testng.annotations.DataProvider;
import org.testng.log4testng.Logger;

/**
 * @DataprivoderIterator<Object[]>
 * @author admin
 *
 */
public class BaseParpare {
	 public static Logger logger = Logger.getLogger(BaseParpare.class);

	    @DataProvider
	public Iterator<Object[]> testData(Method method) {
		/*
		 * TargetTestData annotation = method.getAnnotation(TargetTestData.class);
		 * String simpleName = this.getClass().getSimpleName(); String substring =
		 * simpleName.substring(0, simpleName.indexOf("_")); //
		 * System.out.println(substring); if (annotation == null) { annotation =
		 * (TargetTestData) Proxy.newProxyInstance(BaseParpare.class.getClassLoader(),
		 * new Class[]{TargetTestData.class}, new InvocationHandler() {
		 * 
		 * @Override public Object invoke(Object proxy, Method method, Object[] args)
		 * throws Throwable { if ("sourceType".equals(method.getName())) return
		 * "excel";//默认去excel读取数据 if ("sourcePath".equals(method.getName())) return
		 * "/testData/" + substring + ".xlsx";//如果没有写读取地址，则默认到类名前缀的文件中去找 if
		 * ("sourceSheetOrSql".equals(method.getName())) return "Sheet1"; if
		 * ("mysqlPrefix".equals(method.getName())) return ".test"; return null; } }); }
		 * 
		 * String sourceType = annotation.sourceType(); if ("excel".equals(sourceType))
		 * { return new ExcelDataproviderUtil(annotation.sourcePath(),
		 * annotation.sourceSheetOrSql()); } if ("mysql".equals(sourceType)) { return
		 * new MySQLDataProviderUtil(annotation.sourceSheetOrSql(),
		 * annotation.mysqlPrefix()); }
		 * 
		 * return null;
		 */
	   return null;	
	}
}
