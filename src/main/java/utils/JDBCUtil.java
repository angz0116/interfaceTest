package utils;

import java.sql.*;
import java.util.Map;

public class JDBCUtil {
    private static final String DATABASE= "database";
    private static final String URL="url";
    private static final String PORT = "port";
    private static final String USERNAME="username";
    private static final String PWD="password";
    //private static final String PATH = "/db.properties";
	private static PropertiesUtil propertiesUtil = new PropertiesUtil();
     //保证driver只加载一次
    static {
        try {
        
            Class.forName(propertiesUtil.getProperty(DATABASE));//PropertiesUtil.getProVal(PATH,JDBC_DRIVER)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库链接
     * @return
     */
    public static Connection getConn(String prefix){
        String url = propertiesUtil.getProperty(URL);//PropertiesUtil.getProVal(PATH, prefix+JDBC_URL);
        String username = propertiesUtil.getProperty(USERNAME);//PropertiesUtil.getProVal(PATH, prefix+JDBC_USERNAME);
        String pwd = propertiesUtil.getProperty(PWD);//PropertiesUtil.getProVal(PATH, prefix+JDBC_PWD);
        try {
            Connection connection = DriverManager.getConnection(url, username, pwd);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection conn, ResultSet resultSet, PreparedStatement statement){
        if (conn !=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet!=null)
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (statement!=null)
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

}