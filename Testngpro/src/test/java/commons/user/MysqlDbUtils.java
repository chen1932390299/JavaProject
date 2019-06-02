package commons.user;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;


/*

    Description : retURN ArrayList SQL QUERY Results;

 */

public class MysqlDbUtils {
    private static Logger logger = Logger.getLogger(MysqlDbUtils.class);
    public  ArrayList<Object> select(String url,String user,String pwd,String sql) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Object> list =new ArrayList<>();
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,pwd);
            stmt = conn.createStatement();
            ResultSetMetaData rsmd=null;
            int count=0;
            ResultSet result = stmt.executeQuery(sql);
            rsmd= result.getMetaData();
            count=rsmd.getColumnCount();

            while(result.next()){
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    map.put(rsmd.getColumnLabel(i),result.getObject(i));
                }
                JSONObject json = JSONObject.fromObject(map);
                list.add(json.toString());
            }
            result.close();
            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            try{
                if(stmt!=null){
                    stmt.close();
                }
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        logger.info("goodBye!!");
        return list;
    }

}
