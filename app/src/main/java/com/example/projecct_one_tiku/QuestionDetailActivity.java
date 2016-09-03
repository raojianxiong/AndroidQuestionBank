package com.example.projecct_one_tiku;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import entries.Question;
import singleton.SingleInstance;

/**
 * Created by 饶建雄 on 2016/9/2.
 */
@ContentView(R.layout.activity_question_detail)
public class QuestionDetailActivity extends AppCompatActivity {
    @ViewInject(R.id.quest_title2)
    private TextView quest_title;
    @ViewInject(R.id.question_ask)
    private TextView question_ask;
    @ViewInject(R.id.question_toolbar_prev)
    private TextView pre;
    @ViewInject(R.id.question_toolbar_favorite)
    private TextView favorite;
    @ViewInject(R.id.question_toolbar_next)
    private TextView next;
    @ViewInject(R.id.sv_ask)
    private ScrollView sv_ask;
    @ViewInject(R.id.quest_item_container)
    private LinearLayout quest_container;
    @ViewInject(R.id.questiont_toolBar)
    private Toolbar toolbar;

    private int pos;
    private int totalElements;
    private int typeId;
    private int itemId;
    private Question question;
    private ArrayList<Question.Item> datas;
    private Gson gson;
    private SingleInstance instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.this.finish();
            }
        });
        instance = SingleInstance.newInstance();
        pos = instance.getPos();
        totalElements = instance.getTotalElements();
        itemId = instance.getId();//得到题目的id
        getSupportActionBar().setTitle("第"+(pos+1)+"/"+totalElements+"道题");

        instance = SingleInstance.newInstance();

        loadServerData();


    }
    private void loadServerData(){
        RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/question?method=findone");
        params.addBodyParameter("id",itemId+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                parseData(result);
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
    private void parseData(String result){
        gson = new Gson();
        question = gson.fromJson(result, Question.class);
        quest_title.setText(question.getContent());//内容
        typeId = question.getTypeid();//类型
        itemId = question.getId();//每次题目的id
        initType();
    }
    private void initType(){
        switch (typeId){
            case 1://单选
            case 2://多选
                quest_container.removeAllViews();
                sv_ask.setVisibility(View.GONE);
                quest_container.setVisibility(View.VISIBLE);
                datas = gson.fromJson(question.options, new TypeToken<ArrayList<Question.Item>>(){}.getType());

                System.out.println(datas.toString());
                for(int i = 0;i < datas.size();i++){
                    Question.Item item = datas.get(i);
                    CheckBox cbx = new CheckBox(QuestionDetailActivity.this);
                    cbx.setText((i+1)+item.title);
                    if(item.checked){
                        cbx.setChecked(true);
                    }else{
                        cbx.setChecked(false);
                    }
                    quest_container.addView(cbx);
                }

                break;
            case 3://判断
            case 4://简答
                question_ask.setText(question.getAnswer());
                sv_ask.setVisibility(View.VISIBLE);
                quest_container.setVisibility(View.GONE);

                break;
        }
    }

    @Event(value = {R.id.question_toolbar_prev,R.id.question_toolbar_favorite,R.id.question_toolbar_next},type = View.OnClickListener.class)
    private void onClick(View view){
                 switch (view.getId()){
                     case R.id.question_toolbar_prev:
                         pre();//上一题
                         break;
                     case R.id.question_toolbar_favorite:
                         favorite();//收藏
                         break;
                     case R.id.question_toolbar_next:
                         next();//下一题
                         break;
                 }
    }

    private void pre(){
        if(pos == 0){
            Toast.makeText(QuestionDetailActivity.this, "已是第一题", Toast.LENGTH_SHORT).show();
        }else{
            pos --;
            RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/question?method=prev");
           params.addBodyParameter("id",itemId+"");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    parseData(result);
                    getSupportActionBar().setTitle("第"+(pos+1)+"/"+totalElements+"道题");
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

    private void favorite(){
        int user_id = instance.getUser_id();
        if(favorite.isSelected()){//如果收藏了，点击取消即可
            RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/mng/store?method=delete");
            params.addBodyParameter("userId",user_id+"'");
            params.addBodyParameter("questionId",itemId+"");

            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        System.out.println(result);
                        JSONObject object = new JSONObject(result);
                        if(object.getString("success").equals("true")){
                            Toast.makeText(QuestionDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                      ex.printStackTrace();
                    System.out.println("**************");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else{
            RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/mng/store?method=add");
            params.addBodyParameter("userId",user_id+"");
            params.addBodyParameter("questionId",itemId+"");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        System.out.println(result);
                        //{"msg":"后台操作请先登录","state":"fail"}
                        JSONObject object = new JSONObject(result);
                        if(object.getString("success").equals("true")){
                            favorite.setSelected(true);
                            Toast.makeText(QuestionDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestionDetailActivity.this, "收藏失败，该问题已收藏", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ex.printStackTrace();
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

    private void next(){
        if(pos == totalElements-1){
            Toast.makeText(QuestionDetailActivity.this, "已经是最后一题了", Toast.LENGTH_SHORT).show();
        }else{
            pos++;
            RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/question?method=next");
            params.addBodyParameter("id",itemId+"");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    parseData(result);
                    getSupportActionBar().setTitle("第"+(pos+1)+"/"+totalElements+"道题");
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
}
