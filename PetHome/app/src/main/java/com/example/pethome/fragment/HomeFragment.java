package com.example.pethome.fragment;

import android.content.Context;
import android.graphics.Outline;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.database.lib.SqlQueue;
import com.example.pethome.adapter.ArticleAdapter;
import com.example.pethome.entity.Article;
import com.example.pethome.R;
import com.example.pethome.sql.ArticleHelper;
import com.google.android.material.tabs.TabLayout;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Banner banner;
    private TabLayout tabLayout;
    private ListView lv;
    
    private int selectedTabIndex = 0; // 默认选中第一个选项卡

    private ArticleAdapter articleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        articleAdapter = new ArticleAdapter();
        articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {

            @Override
            public void onItemDeleteClick(int position, Article article) {
                // 确认删除，调用数据库辅助类的方法删除该条动态
                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Void>() {
                    @Override
                    public Void execute() throws Exception {
                        ArticleHelper.deleteById(article.getId());
                        return null;
                    }

                    @Override
                    public void onExecuteSuccess(Void unused) {
                        articleAdapter.removeData(article);
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        initView(v);
        banner();
        showData("");
        setupTabLayoutListener();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新加载数据
        setupTabLayoutListener();

        // 根据选中的选项卡索引重新设置适配器
        switch (selectedTabIndex) {
            case 0:
                showData("");
                break;
            case 1:
                showData("领养");
                break;
            case 2:
                showData("寻宠");
                break;
        }
    }


    private void setupTabLayoutListener() {
        // 添加选项卡选择监听器
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();
                switch (tabText) {
                    case "全部":
                        selectedTabIndex = 0;
                        showData("");
                        break;
                    case "领养":
                        selectedTabIndex = 1;
                        showData("领养");
                        break;
                    case "寻宠":
                        selectedTabIndex = 2;
                        showData("寻宠");
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 未选中选项卡时不执行任何操作
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 重新选择选项卡时不执行任何操作
            }
        });
    }

    // 默认显示所有数据
    private void showData(String type) {
        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<List<Article>>() {
            @Override
            public List<Article> execute() throws Exception {
                if (TextUtils.isEmpty(type)) {
                    return ArticleHelper.getAllData();
                } else {
                    return ArticleHelper.getDataByArticleType(type);
                }
            }

            @Override
            public void onExecuteSuccess(List<Article> articles) {
                articleAdapter.setData(articles);
            }

            @Override
            public void onExecuteError(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void banner() {
        // 添加轮播图数据
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.lb04);
        list.add(R.drawable.lb03);
        list.add(R.drawable.lb01);
        list.add(R.drawable.lb02);
        // 设置轮播图数据
        banner.setImages(list);
        // 设置轮播图圆角
        banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20);
            }
        });
        banner.setClipToOutline(true);

        // 设置轮播图显示
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load((Integer) path).into(imageView);
            }
        });
        banner.start();
    }

    private void initView(View v) {
        banner = v.findViewById(R.id.banner);
        tabLayout = v.findViewById(R.id.tabLayout);
        lv = v.findViewById(R.id.lv);
        lv.setAdapter(articleAdapter);
    }
}