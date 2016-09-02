package com.example.projecct_one_tiku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import utils.DensityUtils;

/**
 * Created by 饶建雄 on 2016/8/29.
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout ll_point;
    private View gray_point;
    private Button btn_start;
    private ArrayList<ImageView> imageViews;
    private int pointWidth;
    private MyGuideAdaper adapter;
    private int[] imageIds = {R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_guide);

        initViews();

        initEvents();
    }
    private void initViews(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        gray_point = findViewById(R.id.gray_point);
        btn_start = (Button) findViewById(R.id.btn_use);
    }
    private void initEvents(){
        imageViews = new ArrayList<ImageView>();
        for(int i = 0;i<imageIds.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            imageViews.add(imageView);
        }
        //设置圆点
        for(int i = 0;i<imageIds.length;i++){
            View point = new View(this);
            point.setBackgroundResource(R.drawable.blue_point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dpToPx(this,10), DensityUtils.dpToPx(this,10));

            if(i>0){
                params.leftMargin = DensityUtils.dpToPx(this,10);
            }
            point.setLayoutParams(params);
            ll_point.addView(point);
        }
        ll_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pointWidth = ll_point.getChildAt(1).getLeft() - ll_point.getChildAt(0).getLeft();
            }
        });

        adapter = new MyGuideAdaper();
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int left = (int) (pointWidth * position + position*positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gray_point.getLayoutParams();
                params.leftMargin = left;
                gray_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                     if(imageIds.length - 1 == position){
                         btn_start.setVisibility(View.VISIBLE);
                     }else{
                         btn_start.setVisibility(View.INVISIBLE);
                     }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    class MyGuideAdaper extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
