package com.example.projecct_one_tiku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import singleton.SingleInstance;
import utils.PreUtils;

/**
 * Created by 饶建雄 on 2016/8/29.
 * 欢迎页面
 */
public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout ll;
    private SingleInstance instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_wel);
        ll = (LinearLayout) findViewById(R.id.ll);

        initAnim();
    }
    private void initAnim(){
        AlphaAnimation alpha = new AlphaAnimation(0.2f,1.0f);
        alpha.setDuration(3000);
        alpha.setFillAfter(true);
        ll.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                  jumpToNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void jumpToNextPage(){
               if(PreUtils.isFirst(this)){
                   startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                   finish();
               }else{
               loginNow();
               }
    }


    //判断是否登陆过，直接登陆
    private void loginNow(){
        String[]  s = PreUtils.getLogin(this);
        if(s == null||s[0]==null||s[1] == null){
           startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else{
            String user_name = s[0];
            String pwd = s[1];
            Toast.makeText(WelcomeActivity.this, "正在登录....请稍后", Toast.LENGTH_SHORT).show();
            loginIn(user_name,pwd);//有延迟,直接跳转吧
//            startActivity(new Intent(this,MainActivity.class));
//            finish();

        }
    }
    /**
     * 登录
     */
    private void loginIn(final String name, final String password){
        instance = SingleInstance.newInstance();
        RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/login");
        params.addBodyParameter("username",name);
        params.addBodyParameter("password",password);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    PreUtils.setLogin(WelcomeActivity.this,name,password);
                    JSONObject object = new JSONObject(result);
                    String success = object.getString("success");
                    if(success.equals("true")){
                        JSONObject object1 = object.getJSONObject("user");
                        String nickname = object1.getString("nickname");
                        instance.setNickName(nickname);//设置昵称
                        System.out.println("设置昵称成功");
                        int user_id = object1.getInt("id");
                        instance.setUser_id(user_id);
                        Toast.makeText(WelcomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                    }else if(success.equals("false")){
                        String reason = object.getString("reason");
                        Toast.makeText(WelcomeActivity.this, "登录失败  "+ reason, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                        finish();
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
}
