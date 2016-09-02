package com.example.projecct_one_tiku;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
@ContentView(R.layout.main_register)
public class RegisterActivity extends AppCompatActivity {
    @ViewInject(R.id.et_username)
    private EditText et_username;
    @ViewInject(R.id.et_pwd)
    private EditText et_pwd;
    @ViewInject(R.id.et_nickname)
    private EditText et_nickname;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.reg_ok)
    private Button reg_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
    @Event(value = R.id.reg_ok,type = View.OnClickListener.class)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.reg_ok:
                String name = et_username.getText().toString();
                String pwd = et_pwd.getText().toString();
                String nickName = et_nickname.getText().toString();
                String phone = et_phone.getText().toString();
                if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(pwd)||TextUtils.isEmpty(nickName)|| TextUtils.isEmpty(phone)){
                    Toast.makeText(RegisterActivity.this, "输入为空，请重新按照要求输入", Toast.LENGTH_SHORT).show();
                }else{
                       if(!isName(name)){
                           Toast.makeText(RegisterActivity.this, "用户名只能用字母以及3位以上！", Toast.LENGTH_SHORT).show();
                       }else if(!isPassword(pwd)){
                           Toast.makeText(RegisterActivity.this, "密码是数字及至少4位以上！", Toast.LENGTH_SHORT).show();
                       }else{
                           register(name,pwd,nickName,phone);
                       }
                }
                //注册
                break;
        }
    }
     private void register(String name,String pwd,String nickName,String phone){
         RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/registe");
         params.addBodyParameter("username",name);
         params.addBodyParameter("password",pwd);
         params.addBodyParameter("nickname",nickName);
         params.addBodyParameter("telephone",phone);
         x.http().post(params, new Callback.CommonCallback<String>() {
             @Override
             public void onSuccess(String result) {
                 try {
                     JSONObject object = new JSONObject(result);
                     String suc = object.getString("success");
                     if(suc.equals("true")){
                         Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                         finish();
                     }else if(suc.equals("false")){
                         Toast.makeText(RegisterActivity.this, "用户名重复", Toast.LENGTH_SHORT).show();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }

             @Override
             public void onError(Throwable ex, boolean isOnCallback) {

             }

             @Override
             public void onCancelled(CancelledException cex) {

             }

             @Override
             public void onFinished() {

             }
         });
     }
    /**
     * 判断电话号码输入是否合法 十一位，是移动联通电信的号码
     * @param mobiles 电话号码
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(18[1,0-1]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 判断密码输入格式是否正确，长度4，包含字母，数字和.
     */
    public static boolean isPassword(String passWord)
    {
        String passwordPattern = "^[0-9\\.]{4,}$";
        return Pattern.matches(passwordPattern, passWord);
    }
    /**
     * 判断用户名输入格式
     */
    public static boolean isName(String name){
        String namePatern = "^[A-Za-z\\.]{3,}$";
        return Pattern.matches(namePatern, name);
    }
    /**
     * 验证手机号码
     *
     * @param mPhone 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(String mPhone) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mPhone);
        return matcher.matches();
    }

    /**
     * 验证密码
     *
     * @param mPassword
     * @return
     */

    private Boolean checkPassword(String mPassword) {
        if (mPassword.length() >= 4) {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher matcher = pattern.matcher(mPassword);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 验证用户名
     *
     * @param mUsername
     * @return
     */
    private Boolean checkUsername(String mUsername) {
        if (mUsername.length() > 3) {
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            Matcher matcher = pattern.matcher(mUsername);
            return matcher.matches();
        }
        return false;
    }

}
