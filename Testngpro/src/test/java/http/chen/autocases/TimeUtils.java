package http.chen.autocases;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.RandomStringUtils;

public class TimeUtils {
    static String time=null;
    public static void main(String[] args ) {
        System.out.println(getLocaltime());
        System.out.println(getfurTime(0, 0, 0, 0, 2, 0));
        System.out.println(comparetime(getLocaltime(),getfurTime(0, 0, 0, 0, 2, 0)));
        System.out.println(getrdnum());
    }
    public static String getLocaltime() {
        Calendar calendar =Calendar.getInstance();
        time=(new SimpleDateFormat(("yyyy-MM-dd  HH:mm:ss:SS"))).format(calendar.getTime());
        return time;
    }
    public static String  getfurTime(int Y,int M ,int Date,int h,int m,int s) {
        Calendar calendar =Calendar.getInstance();
        calendar.add(Calendar.YEAR,Y);
        calendar.add(Calendar.MONTH, M);
        calendar.add(Calendar.DAY_OF_MONTH, Date);
        calendar.add(Calendar.HOUR_OF_DAY, h);
        calendar.add(Calendar.MINUTE, m);
        calendar.add(Calendar.SECOND, s);
        time=(new SimpleDateFormat(("yyyy-MM-dd  HH:mm:ss:SS"))).format(calendar.getTime());
        return time;
    }
    public static boolean comparetime(String curtime,String furtime) {
        boolean bool=false;
        int res=curtime.compareTo(furtime);
        if(res<=0) {
            bool=true;
        }else if(res>0) {
            bool=false;
        }
        return  bool;
    }
    public static String getrdString() {
        String rd=RandomStringUtils.random(11, true, true);
        return rd;
    }
    public static String getrdnum() {
        String rd=RandomStringUtils.random(0, 2, 3, false, true);
        return rd;
    }
}
