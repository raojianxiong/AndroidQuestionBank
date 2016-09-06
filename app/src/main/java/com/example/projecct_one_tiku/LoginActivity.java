package com.example.projecct_one_tiku;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import singleton.SingleInstance;
import utils.PreUtils;

@ContentView(R.layout.main_login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.user_name)
    private EditText userName;
    @ViewInject(R.id.password)
    private EditText password;
    @ViewInject(R.id.btn_login)
    private Button btn_login;
    @ViewInject(R.id.forget_password)
    private TextView forget_password;
    @ViewInject(R.id.register_account)
    private TextView register_account;
    private SingleInstance instance;
    private AlertDialog show;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        instance = SingleInstance.newInstance();


    }
    @Event(value = {R.id.btn_login,R.id.forget_password,R.id.register_account},type = View.OnClickListener.class)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                //登录时实现加载进度未写
                String name = userName.getText().toString();
                String pass = password.getText().toString();
                loginIn(name,pass);
                //登录
                break;
            case R.id.register_account:
                //注册
                register();
                break;
            case R.id.forget_password:
                //忘记密码
                Toast.makeText(LoginActivity.this, "本网站需要手持身份证到服务区申请重置密码", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    /**
     * 登录
     */
    private void loginIn(final String name, final String password){
//        showDialog();
        showPop();
        RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/login");
        params.addBodyParameter("username",name);
        params.addBodyParameter("password",password);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        PreUtils.setLogin(LoginActivity.this,name,password);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("success");
                        if(success.equals("true")){
                            JSONObject object1 = object.getJSONObject("user");
                            String nickname = object1.getString("nickname");
                            instance.setNickName(nickname);//设置昵称
                            System.out.println("设置昵称成功");
                            int user_id = object1.getInt("id");
                            instance.setUser_id(user_id);
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else if(success.equals("false")){
                            String reason = object.getString("reason");
                            Toast.makeText(LoginActivity.this, "登录失败  "+ reason, Toast.LENGTH_SHORT).show();
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
//                       show.dismiss();
                    popupWindow.dismiss();
                }
            });
    }
    /**
     * 注册
     */
    private void register(){
        startActivity(new Intent(this,RegisterActivity.class));
    }

/**
 * 对话框
 */
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog,null);
        builder.setView(view);
        show = builder.show();
    }

    private void showPop(){
        View view = View.inflate(this,R.layout.dialog,null);
        popupWindow = new PopupWindow(view,200,200,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation((ViewGroup)findViewById(android.R.id.content), Gravity.CENTER,0,0);
    }
}
