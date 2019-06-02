package http.chen.autocases;
import org.apache.commons.lang.RandomStringUtils;

public class StringUserUtils {
    public static String getrdString() {
        String rd=RandomStringUtils.random(11, true, true);
        return rd;
    }
    public static String getrdnum() {
        String rd=RandomStringUtils.random(0, 2, 3, false, true);
        return rd;
    }
}
