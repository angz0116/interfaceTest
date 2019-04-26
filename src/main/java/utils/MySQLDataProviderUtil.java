package utils;
import utils.JDBCUtil;
import java.sql.*;
import java.util.*;

public class MySQLDataProviderUtil implements Iterator<Object[]> {
    private String sql;
    private String prefix;
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String [] columnName;
    private Map<String, ResultSet> sureInitMap = new HashMap<String, ResultSet>();

    /**
     * 构造函数
     * @param sql
     * @param prefix
     */
    public MySQLDataProviderUtil(String sql,String prefix) {
        this.sql = sql;
        this.prefix = prefix;
    }


    /**
     * 确保一个sql不去执行多遍
     */
    private void sureInit() {
        if (sureInitMap.get(sql) == null)
            doSelect();
    }

    /**
     * 执行查询
     */
    private void doSelect() {
        //System.out.println("我的执行次数");
        conn = JDBCUtil.getConn(prefix);
        try {
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();
            sureInitMap.put(sql, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSetMetaData metaData = null;
        int columnCount = 0;
        try {
            metaData = resultSet.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        columnName = new String[columnCount];
        for (int i = 0; i < columnName.length; i++) {
            try {
                columnName[i] = metaData.getColumnName(i + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       // System.out.println("执行结束，获得了resultSet");
    }

    Boolean flag = false;//消除next带来的副作用
    @Override
    public boolean hasNext() {
    	boolean temp = false;
        sureInit();
        if (flag) {
            return true;
        }
        try {
            temp = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (temp) {
            flag = true;
            return true;
        } else {
            close();//当没有下一行数据时，自动关闭数据库各种连接。如果在next=true时，就不再取数据了，建议手动调用close方法，去关闭数据库连接。
            return false;
        }
    }

    @Override
    public Object[] next() {
        sureInit();
        flag = false;
        Map<String, String> testData = new HashMap<>();
        for (int i = 0; i < columnName.length; i++) {//遍历，并获取到当前行的数据
            try {
                String setString = resultSet.getString(i + 1);
                testData.put(columnName[i], setString);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return new Object[]{testData};
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("不支持删除操作");

    }

    /**
     * 对外提供一个关闭数据库的方法，可以手动关闭。
     */
    public void close(){
        JDBCUtil.close(conn,resultSet,statement);
    }
    
    public static void main(String[] args) {
    	MySQLDataProviderUtil mysqlDataproviderUtil = new MySQLDataProviderUtil("SELECT * FROM `Organizers`", "test.");
        for (int i = 0; i < 3; i++) {
            if (mysqlDataproviderUtil.hasNext()){
                Object[] next = mysqlDataproviderUtil.next();
                System.out.println(Arrays.toString(next));}
        }
    }
}
