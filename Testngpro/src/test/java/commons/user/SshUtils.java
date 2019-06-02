package commons.user;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.*;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
public class SshUtils {
    /*
     Description:
         public static void main(String[] args) {
        SshTest.exeCmd("df -m","root","123456","192.168.229.128", 22);
    }
     */
    private static Logger logger = Logger.getLogger(SshUtils.class);
    public static Connection getConnect(String user,String password ,String ip,int port ) {
        Connection conn=new Connection(ip, port);
        try {

            conn.connect();
            conn.authenticateWithPassword(user, password);
            logger.error("ssh connect ok");

        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static String exeCmd(String cmd,String user,String password ,String ip,int port){
        String line=null;
        Connection connection=null;
        Session session=null;
        try {
            connection=getConnect(user, password, ip, port);
            session=connection.openSession();
            session.execCommand(cmd);
            InputStream in = new StreamGobbler(session.getStdout());
            BufferedReader brs = new BufferedReader(new InputStreamReader(in));
            line = brs.readLine();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(session !=null) {
                try {
                    session.close();
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return line;
    }
    // downLoadFile from Linux
    public static boolean sftpDownload(String remoteFilePath,String localFilePath,String user,String password ,String ip,int port) {
        boolean bool=false;
        Connection connection=null;
        try {
            connection=getConnect(user, password, ip, port);
            SCPClient scpClient = connection.createSCPClient();
            scpClient.put(localFilePath, remoteFilePath);
            bool=true;
        }catch(IOException ioe) {
            ioe.printStackTrace();
            bool =false;
        }finally {
            if(connection !=null) {
                connection.close();
            }
        }
        return bool;
    }

    // uploadFile to Linux
    public static boolean uoloadFile(String remoteFilePath,String localFilePath,String user,String password ,String ip,int port) {
        boolean bool=false;
        Connection connection=null;
        try {
            connection=getConnect(user, password, ip, port);
            SCPClient scpClient = connection.createSCPClient();
            scpClient.get(remoteFilePath, localFilePath);
            bool=true;
        }catch(IOException ioe) {
            ioe.printStackTrace();
            bool =false;
        }finally {
            if(connection !=null) {
                connection.close();
            }
        }
        return bool;
    }
}
