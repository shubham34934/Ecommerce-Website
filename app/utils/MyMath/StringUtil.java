package utils.MyMath;

import java.util.List;

/**
 * Created by manjeet on 14/08/15.
 */
public class StringUtil {

    public static String clearSpecialChar(String s) {
        return s.replace(' ','_');
    }

    public static String toString(List<String> s) {
        String res = "";
        for(int i=0; i< s.size(); i++) {
            res += s.get(i)+",";
        }
        if(res != "") {
           // s.remove()
        }
        return res;
    }
}
