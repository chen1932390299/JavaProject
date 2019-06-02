package commons.user;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

public class OrcDbUtils {
    private static Logger logger = Logger.getLogger(OrcDbUtils.class);
    private String url;
    private  String user;
    private String pwd;
    private String sql;
    private Connection connect;
    private PreparedStatement pre ;// 创建预编译语句对象,一般都是用这个而不用Statement
    private ResultSet result;



    public  OrcDbUtils(String url,String user,String pwd){
        this.url=url;
        this.user=user;
        this.pwd=pwd;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connect=DriverManager.getConnection(this.url, this.user, this.pwd);
        }catch (Exception e) {
            logger.info("连接数据驱动失败");
            e.printStackTrace();

        }
    }
    public void CloseDb(){
        try {
            if(this.pre !=null) {
                this.pre.close();
               logger.info("关闭事物");
            }
            if(this.result !=null) {
                this.result.close();
               logger.info("关闭结果对象");
            }
            if(this.connect !=null) {
                this.connect.close();
               logger.info("关闭连接");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Description :select sql method
     */
    public ArrayList<Object> exeselect(String sql) {
        ArrayList<Object> list =new ArrayList<Object>();
        try {
            this.pre=this.connect.prepareStatement(sql);
            this.result = this.pre.executeQuery();
            ResultSetMetaData rsmd=null;
            int count=0;

            rsmd = this.result.getMetaData();
            count=rsmd.getColumnCount();
            while(this.result.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= count; i++) {
                    map.put(rsmd.getColumnLabel(i),this.result.getObject(i));
                }
                JSONObject json = JSONObject.fromObject(map);
                list.add(json.toString());

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {

            this.CloseDb();

        }
        return list;
    }

    /*
        Description :delete and update and insert
     */
    public boolean DeleteOrUpdateInsert(String sql) {
        boolean bool;
        try {
            this.pre=this.connect.prepareStatement(sql);
            this.pre.executeUpdate();
           logger.info("操作成功！！！！！！");
            bool =true;
        }catch (Exception e) {
            bool=false;
            e.printStackTrace();
        }finally {
            this.CloseDb();
        }
        return bool;
    }



    /*
  Usage:
      public static void main(String[] args){
        String url="jdbc:oracle:thin:@localhost:1521/orcl";
        String user="SCOTT";
        String pwd="pipeline";
        String sql="select * from bonus";
        OrcTest ob= new OrcTest(url, user, pwd);
       logger.info(ob.exeselect(sql));
       logger.info(ob.DeleteOrUpdateInsert("insert into bonus(ENAME,JOB,SAL,COMM)VALUES
       ('del','job_del',122,3)"));
    }

 */

}
