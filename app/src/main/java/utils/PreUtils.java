package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 饶建雄 on 2016/8/29.
 */
public class PreUtils {
    //判断是否第一次
    public static boolean isFirst(Context context){
        SharedPreferences sp = context.getSharedPreferences("welcome",Context.MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst",true);
        if(isFirst){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirst",false).commit();
            return true;//是第一次
        }else{
            return false;//false代表不是第一次进入
        }

    }
    //存储用户名和密码
    public static void setLogin(Context context,String name,String pwd){
        SharedPreferences sp = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",name);
        editor.putString("pwd",pwd);
        editor.commit();
    }
    public static String[] getLogin(Context context){
        String[] s = new String[2];
        SharedPreferences sp = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        s[0] = sp.getString("username",null);
        s[1] = sp.getString("pwd","null");
        return s;
    }
    //清除密码账号缓存
    public static void deleteCache(Context context){
       setLogin(context,null,null);
    }
}
