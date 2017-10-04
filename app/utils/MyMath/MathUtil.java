package utils.MyMath;

/**
 * Created by manjeet on 14/08/15.
 */

import java.util.Random;

public class MathUtil {
    static Random randomGenerator = new Random();

    //min inclusive and max exclusive , e.g = 2-5 ... 2+(0,2)
    public static int getRandom(int min, int max) {
        return min + randomGenerator.nextInt(max-min);
    }
}
