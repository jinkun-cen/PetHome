package com.example.pethome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.database.lib.SqlQueue;
import com.example.pethome.activity.AddActivity;
import com.example.pethome.adapter.ForumAdapter;
import com.example.pethome.R;
import com.example.pethome.entity.Forum;
import com.example.pethome.sql.ForumHelper;

import java.util.List;

public class ForumFragment extends Fragment {

    private ImageView imageView19;
    private ImageView imgAdd;
    private ImageView imgBack;
    private ListView lv;

    private ForumAdapter forumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lun_tan, container, false);
        forumAdapter = new ForumAdapter();
        forumAdapter.setOnItemClickListener(new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position, Forum forum) {
                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Void>() {
                    @Override
                    public Void execute() throws Exception {
                        // 确认删除，调用数据库辅助类的方法删除该条动态
                        ForumHelper.deleteById(forum.getId());
                        return null;
                    }

                    @Override
                    public void onExecuteSuccess(Void unused) {
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        forumAdapter.removeData(forum);
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        initView(v); // 初始化界面控件
        show(); // 显示内容
        add(); // 设置添加按钮的点击事件
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 重新加载数据并更新界面
        show();
    }

    private void show() {
        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<List<Forum>>() {
            @Override
            public List<Forum> execute() throws Exception {
                List<Forum> list = ForumHelper.getAllData();
                if (list.isEmpty()) {
                    throw new Exception("暂无数据");
                }
                return list;
            }

            @Override
            public void onExecuteSuccess(List<Forum> forums) {
                forumAdapter.setData(forums); // 设置数据
            }

            @Override
            public void onExecuteError(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void add() {
        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddActivity.class); // 创建意图，跳转到添加动态的页面
            startActivity(intent); // 启动新的活动
        });
    }

    private void initView(View v) {
        imageView19 = v.findViewById(R.id.imageView19);
        imgAdd = v.findViewById(R.id.img_add);
        imgBack = v.findViewById(R.id.img_back);
        lv = v.findViewById(R.id.lv);
        lv.setAdapter(forumAdapter);
    }
}