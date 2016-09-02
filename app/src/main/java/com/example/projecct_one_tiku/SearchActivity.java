package com.example.projecct_one_tiku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import entries.Page;
import singleton.SingleInstance;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity {
    @ViewInject(R.id.et_search)
    private EditText et_search;
    @ViewInject(R.id.tv_search_btn)
    private Button search_btn;
    @ViewInject(R.id.lv_search)
    private ListView lv_search;
    @ViewInject(R.id.search_toolBar)
    private Toolbar search_toolBar;

    private Page mPage;
    private List<Page.ContentBean> contentBeens;
    private MySearchAdapter adapter;
    private SingleInstance instance;

    private int page = 1;
    private int totalElements = 0;
    private int totalPages = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        search_toolBar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(search_toolBar);
        search_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        getSupportActionBar().setTitle("题目查找");
        instance = SingleInstance.newInstance();
    }
    @Event(value = R.id.tv_search_btn,type = View.OnClickListener.class)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.tv_search_btn:
                String questionName = et_search.getText().toString();
                loadServerDatas(questionName);
                break;
        }
    }
    private void loadServerDatas(String questionName){
        RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/question?method=list");
        params.addBodyParameter("page",page+"");
        params.addBodyParameter("questionName",questionName);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                    mPage = gson.fromJson(result, Page.class);
                    contentBeens = mPage.getContent();
                    totalPages= mPage.getTotalPages();//总页数
                    totalElements = mPage.getTotalElements();//总数
                    if(contentBeens!=null){
                        //设置适配器
                        adapter = new MySearchAdapter();
                        lv_search.setAdapter(adapter);
                    }
                lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it = new Intent(SearchActivity.this,QuestionDetailActivity.class);
                        instance.setPos(position);
                        instance.setTotalElements(totalElements);
                        instance.setId(contentBeens.get(position).getId());
                        startActivity(it);
                    }
                });

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

    class MySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contentBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return contentBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = View.inflate(getApplicationContext(),R.layout.listview_questlist_item,null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_pubtime = (TextView) convertView.findViewById(R.id.tv_pubTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(contentBeens.get(position).getContent());
            holder.tv_type.setText(jugeType(contentBeens.get(position).getTypeid()));
            //把时间设置为yyyy-MM-dd形式
            holder.tv_pubtime.setText(DateUtils.formatDateTime(getApplicationContext(),contentBeens.get(position).getPubTime(),DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_DATE));

            return convertView;
        }
        class ViewHolder{
            TextView tv_title;
            TextView tv_type;
            TextView tv_pubtime;
        }
    }
    public String jugeType(int i){
        String s = "";
        switch (i){
            case 1:
                s = "单选";
                break;
            case 2:
                s = "多选";
                break;
            case 3:
                s = "判断";
                break;
            case 4:
                s = "简答";
                break;
        }
        return s;
    }
}
