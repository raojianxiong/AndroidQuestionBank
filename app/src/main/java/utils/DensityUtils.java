package utils;

import android.content.Context;

/**
 * Created by 饶建雄 on 2016/8/29.
 */
public class DensityUtils {
    public static int dpToPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density*dp+0.5f);
    }
}
