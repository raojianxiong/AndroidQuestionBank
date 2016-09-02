package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecct_one_tiku.QuestListActivity;
import com.example.projecct_one_tiku.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import entries.Catelog;

/**
 * Created by 饶建雄 on 2016/8/30.
 * 类别列表
 */
public class CategoryFragment extends Fragment {

    private View view;
    private GridView mGridView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Catelog> data = new ArrayList<Catelog>();
    private MyAdapter adapter;
    private static String url = "http://115.29.136.118:8080/web-question/app/catalog?method=list";
    private static String imgUrl = "http://115.29.136.118:8080/web-question/";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_quest_category,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSwipe(view);

        loadServerData();
    }
    private void initSwipe(View view){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color1,R.color.color2,R.color.color3,R.color.color4);

       mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               mSwipeRefreshLayout.setRefreshing(true);
               System.out.println("开始刷新....");
               loadServerData();

           }
       });

    }
    private void initView(View view){
             mGridView = (GridView) view.findViewById(R.id.grid_view);
        if(data!=null){
            adapter = new MyAdapter();
            mGridView.setAdapter(adapter);
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryFragment.this.getContext(), QuestListActivity.class);
                //传值
                Catelog catelog = data.get(position);
                intent.putExtra("cate_id",catelog.id);
                intent.putExtra("cate_name",catelog.name);
                startActivity(intent);
            }
        });
    }
    private void loadServerData(){
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                data = gson.fromJson(result,new TypeToken<ArrayList<Catelog>>(){}.getType());
                initView(view);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(CategoryFragment.this.getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(),R.layout.gridview_item, null);
                holder = new ViewHolder();
                holder.iv_cate_icon = (ImageView) convertView.findViewById(R.id.iv_cate_icon);
                holder.tv_cate_title = (TextView) convertView.findViewById(R.id.tv_cate_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Catelog item = data.get(position);
            x.image().bind(holder.iv_cate_icon,imgUrl+item.icon);
            holder.tv_cate_title.setText(item.name);
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_cate_icon;
            TextView tv_cate_title;
        }
    }
}
