package utils;
import utils.PropertiesUtil;

import java.sql.*;
import java.util.Map;

public class JDBCUtil {
    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL="jdbc.url";
    private static final String JDBC_USERNAME="jdbc.username";
    private static final String JDBC_PWD="jdbc.password";
    private static final String PATH = "/db.properties";
	ObjectBase objbase = new ObjectBase();
	Map<String,String> hm =  objbase.db;
//保证driver只加载一次
    static {
        try {
            Class.forName(JDBC_DRIVER);//PropertiesUtil.getProVal(PATH,JDBC_DRIVER)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库链接
     * @return
     */
    public static Connection getConn(String prefix){
        String url = JDBC_URL;//PropertiesUtil.getProVal(PATH, prefix+JDBC_URL);
        String username = JDBC_USERNAME;//PropertiesUtil.getProVal(PATH, prefix+JDBC_USERNAME);
        String pwd = JDBC_PWD;//PropertiesUtil.getProVal(PATH, prefix+JDBC_PWD);
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