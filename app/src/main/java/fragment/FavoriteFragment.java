package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projecct_one_tiku.QuestionDetailActivity;
import com.example.projecct_one_tiku.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import entries.Page;
import singleton.SingleInstance;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
public class FavoriteFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mlistView;
    private SingleInstance instance;
    private int userId;

    private List<Page.ContentBean> mContentBeans;
    private Page mPage;
    private MyFavoriteAdapter adapter;
    private int totalElements = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite_page,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
    }
    private void initDatas(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.favorite_swipe_refresh);
        mlistView = (ListView) view.findViewById(R.id.lv_favorite);

        instance = SingleInstance.newInstance();
        userId = instance.getUser_id();

        loadServerData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadServerData();

            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(),QuestionDetailActivity.class);
                instance.setPos(position);
                instance.setTotalElements(totalElements);
                instance.setId(mContentBeans.get(position).getId());
                startActivity(it);
            }
        });
    }
    private void loadServerData(){
        RequestParams params = new RequestParams("http://115.29.136.118:8080/web-question/app/mng/store?method=list");
        params.addBodyParameter("userId",userId+"");
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mPage = gson.fromJson(result, Page.class);
                mContentBeans = mPage.getContent();
                totalElements = mPage.getTotalElements();
                if(mContentBeans!=null){
                    adapter = new MyFavoriteAdapter();
                    mlistView.setAdapter(adapter);
                }
                mSwipeRefreshLayout.setRefreshing(false);
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

    class MyFavoriteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContentBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mContentBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = View.inflate(getActivity(),R.layout.listview_questlist_item,null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_pubtime = (TextView) convertView.findViewById(R.id.tv_pubTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(mContentBeans.get(position).getContent());
            holder.tv_type.setText(jugeType(mContentBeans.get(position).getTypeid()));
            //把时间设置为yyyy-MM-dd形式
            holder.tv_pubtime.setText(DateUtils.formatDateTime(getActivity(),mContentBeans.get(position).getPubTime(),DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_DATE));

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
