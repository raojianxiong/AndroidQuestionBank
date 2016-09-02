package com.example.projecct_one_tiku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import entries.Page;
import singleton.SingleInstance;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
@ContentView(R.layout.activtiy_quest_list)
public class QuestListActivity extends AppCompatActivity {
    @ViewInject(R.id.swipe_refresh)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewInject(R.id.lv)
    private ListView mListView;
    @ViewInject(R.id.quest_title)
    private TextView quest_title;
    @ViewInject(R.id.quest_image)
    private ImageView image_back;

    private Intent intent;
    private String title ;
    private int cate_id;
    private int page = 1;//当前页数
    private int totalPages = 1;//总页数
    private int totalElements;//总数
    private Page mPage;
    private List<Page.ContentBean> contentBeens;
    private static String url = "http://115.29.136.118:8080/web-question/app/question?method=list";
    private MyAdapter adapter;

    private SingleInstance instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initEvents();
    }
    private void initEvents(){
        instance = SingleInstance.newInstance();
        intent = getIntent();
        cate_id = intent.getIntExtra("cate_id",0);
        title = intent.getStringExtra("cate_name");
        quest_title.setText(title);

        loadServerData(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                if(page<totalPages){
                    loadServerData(false);//第二次刷新加载第二个页面
                }else{
                    Toast.makeText(QuestListActivity.this, "已经全部加载完", Toast.LENGTH_SHORT).show();
                     mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it = new Intent(QuestListActivity.this,QuestionDetailActivity.class);
                       instance.setPos(position);
                 instance.setTotalElements(totalElements);
                instance.setId(contentBeens.get(position).getId());
                startActivity(it);
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestListActivity.this.finish();
            }
        });
    }
    private void loadServerData(final boolean isFirst){
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("catalogId",cate_id+"");
        params.addBodyParameter("page",page+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                parseData(result,isFirst);
                mSwipeRefreshLayout.setRefreshing(false);//加载成功就停止刷新
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
    private void parseData(String result,boolean isFirst){
        Gson gson = new Gson();
        if(isFirst){//第一次加载
           mPage = gson.fromJson(result, Page.class);
            contentBeens = mPage.getContent();
            totalPages= mPage.getTotalPages();//总页数
            totalElements = mPage.getTotalElements();//总数
            if(contentBeens!=null){
                //设置适配器
                adapter = new MyAdapter();
                mListView.setAdapter(adapter);
            }
        }else{//如果是第二次
            mPage = gson.fromJson(result, Page.class);
            contentBeens.addAll(mPage.getContent());
            adapter.notifyDataSetChanged();
        }
    }
    class MyAdapter extends BaseAdapter{

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
