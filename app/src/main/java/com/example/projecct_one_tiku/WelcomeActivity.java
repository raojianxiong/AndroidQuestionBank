package com.example.projecct_one_tiku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import utils.PreUtils;

/**
 * Created by 饶建雄 on 2016/8/29.
 * 欢迎页面
 */
public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout ll;

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
                   startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                   finish();
               }
    }
}
