package com.example.projecct_one_tiku;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import entries.MenuListViewItem;
import fragment.CategoryFragment;
import fragment.FavoriteFragment;
import singleton.SingleInstance;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.container)
    private FrameLayout container;

    @ViewInject(R.id.ll)
    private LinearLayout ll;//侧滑菜单布局
    @ViewInject(R.id.listView)
    private ListView mDrawerList;
    @ViewInject(R.id.circleView)
    private CircleImageView mCircleImageView;
    @ViewInject(R.id.tv_nickname)
    private TextView nickName;
    @ViewInject(R.id.tv_setting)
    private TextView tv_setting;
    @ViewInject(R.id.tv_exit)
    private TextView tv_exit;
    @ViewInject(R.id.tool_bar)
    private Toolbar tool_bar;

//    DrawerArrowDrawable drawerArrow;
    ActionBarDrawerToggle mDrawerToggle;
    //是否打开菜单
    boolean openOrClose;
    MyAdapter adapter;
    private SingleInstance instance;

    ArrayList<MenuListViewItem> items = new ArrayList<MenuListViewItem>();
    FragmentManager fm;
    long temp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);


        setSupportActionBar(tool_bar);

        fm = getSupportFragmentManager();

        initData();

    }

    private void initData(){
        items.add(new MenuListViewItem(R.mipmap.home_nav_icon01,"分类查找"));
        items.add(new MenuListViewItem(R.mipmap.home_nav_icon02,"题目查找"));
        items.add(new MenuListViewItem(R.mipmap.home_nav_icon03,"我的成就"));
        items.add(new MenuListViewItem(R.mipmap.home_nav_icon04,"我的收藏"));


        initMenu();
    }
    private void initMenu() {
          if(items.size()!=0){
              System.out.println("=====不为空======");
              adapter = new MyAdapter();
              mDrawerList.setAdapter(adapter);
              mDrawerList.setItemChecked(0,true);//默认选择第一项
          }
        changeFragment(new CategoryFragment());
        getSupportActionBar().setTitle("分类练习");

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //切换页面
                switch (position){
                    case 0:
                        changeFragment(new CategoryFragment());
                        getSupportActionBar().setTitle("分类练习");
                        break;
                    case 1:
                        //题目查找
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        break;
                    case 2:
                        getSupportActionBar().setTitle("我的成就");
                        break;
                    case 3:
                        changeFragment(new FavoriteFragment());
                        getSupportActionBar().setTitle("我的收藏");
                        break;
                }
                mDrawerLayout.closeDrawers();//关闭抽屉
                openOrClose = false;//关闭
                          }
        });
        instance = SingleInstance.newInstance();
        if(instance.getNickName()!=null){
            nickName.setText(instance.getNickName());//单例
        }
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                mDrawerLayout.closeDrawers();
                openOrClose = false;
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,tool_bar,R.string.open_drawer,R.string.close_drawer){
           @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                    drawerView.setClickable(true);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_search){
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(),R.layout.listview_menu_item,null);
                    holder = new ViewHolder();
                    holder.image = (ImageView) convertView.findViewById(R.id.iv_pic);
                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_title);

                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
            MenuListViewItem item = items.get(position);
            holder.image.setImageResource(item.pic);
            holder.tv_content.setText(item.title);
            return convertView;
        }
        private class ViewHolder{
            ImageView image;
            TextView tv_content;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if (openOrClose) {
                 mDrawerLayout.closeDrawers();
            }else{
                long curr = System.currentTimeMillis();
                if(curr - temp < 3000){//第一次肯定大于3000毫秒
                    MainActivity.this.finish();
                }else{
                    temp = curr;
                    Toast.makeText(MainActivity.this, "再按一次退出!", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
