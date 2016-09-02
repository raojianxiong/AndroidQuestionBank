package com.example.projecct_one_tiku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import utils.PreUtils;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
@ContentView(R.layout.setting_page)
public class SettingActivity extends AppCompatActivity {
    @ViewInject(R.id.setting_nick_name)
    private TextView setting_nick_name;

    @ViewInject(R.id.login_auto)
    private TextView login_auto;
    @ViewInject(R.id.cb_login_auto)
    private CheckBox cb_login_auto;

    @ViewInject(R.id.show_photo)
    private TextView show_photo;
    @ViewInject(R.id.cb_show_photo)
    private CheckBox cb_show_photo;

    @ViewInject(R.id.quit_login)
    private TextView quit_login;

    @ViewInject(R.id.delete_cache)
    private TextView delete_cache;

    @ViewInject(R.id.about_us)
    private TextView about_us;

    @ViewInject(R.id.setting_toolBar)
    private Toolbar setting_toolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        setSupportActionBar(setting_toolBar);
        setting_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
    }

    @Event(value = {R.id.setting_nick_name, R.id.cb_login_auto, R.id.cb_show_photo, R.id.quit_login, R.id.delete_cache, R.id.about_us}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_nick_name:
                setNickName();
                break;
            case R.id.cb_login_auto://自动开启登录
                if (cb_login_auto.isChecked()) {
                    login_auto.setText("已开启");
                } else {
                    //未登录状态既是把内存cacahe清理一下,账号用户名删除
                    PreUtils.deleteCache(this);
                    login_auto.setText("未开启");
                }
                break;
            case R.id.cb_show_photo:
                if (cb_show_photo.isChecked()) {
                    show_photo.setText("在3G/4G和WIFI下都显示图片");
                } else {
                    show_photo.setText("仅通过WIFI");
                }
                break;
            case R.id.quit_login:
                PreUtils.deleteCache(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.delete_cache:
                PreUtils.deleteCache(this);
                Toast toastl = new Toast(this);
                LinearLayout lll = new LinearLayout(this);
                lll.setBackgroundResource(R.drawable.dialog_bg);
                TextView tvl = new TextView(this);
                LinearLayout.LayoutParams paramsl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvl.setLayoutParams(paramsl);
                tvl.setTextColor(Color.WHITE);
                tvl.setTextSize(22);
                tvl.setText("正在清除....");
                lll.addView(tvl);
                toastl.setView(lll);
                toastl.setDuration(Toast.LENGTH_LONG);
                toastl.show();
                break;
            case R.id.about_us:
                Toast toast = new Toast(this);
                LinearLayout ll = new LinearLayout(this);
                ll.setBackgroundResource(R.drawable.dialog_bg);
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(params);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(22);
                tv.setText("启动关于我们的界面....");
                ll.addView(tv);
                toast.setView(ll);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }

    String s = "xiaoli";

    private void setNickName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("昵称设置");
        final EditText editText = new EditText(this);
        editText.setTextColor(Color.BLACK);
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setText(s);
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s = editText.getText().toString().trim();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
            }
        });
        builder.show();
    }
}
