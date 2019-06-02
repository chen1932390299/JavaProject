package http.chen.autocases;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferUtils {
    /*
         Description: string to map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> stringToMap(String jsonString) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map = gson.fromJson(jsonString, map.getClass());
        return map;
    }

    /*
      Description: json to string
     */
    public static String jsonToString(JSONObject jsonObject) {

        return jsonObject.toString();
    }

    /*

      Description: string to json
     */
    public static JSONObject strtoJson(String str) {

        return JSONObject.fromObject(str);
    }

    /*

      Description: json String to  java ArrayList
    */
    public static ArrayList<Object> jsonToArrayList(JSONObject jsonObject) {

        JSONArray jArray = JSONArray.fromObject(jsonObject);
        ArrayList<Object> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.size(); i++) {
                listdata.add(jArray.getString(i));
            }
        }
        return listdata;
    }

    /*
    Description: json to  java array
    */

    public static String maptoString(Map<Object, Object> map) {
        return map.toString();
    }


}
