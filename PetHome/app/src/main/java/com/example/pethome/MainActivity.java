package com.example.pethome;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pethome.fragment.PublishFragment;
import com.example.pethome.fragment.HomeFragment;
import com.example.pethome.fragment.ForumFragment;
import com.example.pethome.fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fr;
    private LinearLayout llHome;
    private ImageView imgHome;
    private TextView tvHome;
    private LinearLayout llFabu;
    private ImageView imgFabu;
    private TextView tvFabu;
    private LinearLayout llLuntan;
    private ImageView imgLuntan;
    private TextView tvLuntan;
    private LinearLayout llMine;
    private ImageView imgMine;
    private TextView tvMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Navigation();
        String mine = getIntent().getStringExtra("mine");
        if (mine == null||mine.isEmpty()) {
            llHome.callOnClick();
        } else {
            if (mine.equals("fb")) {
                llFabu.callOnClick();
            } else if (mine.equals("lt")) {
                llLuntan.callOnClick();
            }
        }

    }

    // 点击控件进行页面转换
    private void Navigation() {
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(0);
                imgHome.setImageResource(R.drawable.xhome);
                tvHome.setTextColor(Color.parseColor("#f5cc5b"));
                imgFabu.setImageResource(R.drawable.fabu);
                tvFabu.setTextColor(Color.parseColor("#bfbfbf"));
                imgLuntan.setImageResource(R.drawable.luntan);
                tvLuntan.setTextColor(Color.parseColor("#bfbfbf"));
                imgMine.setImageResource(R.drawable.mine);
                tvMine.setTextColor(Color.parseColor("#bfbfbf"));
            }
        });
        llFabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(1);
                imgHome.setImageResource(R.drawable.home);
                tvHome.setTextColor(Color.parseColor("#bfbfbf"));
                imgFabu.setImageResource(R.drawable.xfabu);
                tvFabu.setTextColor(Color.parseColor("#f5cc5b"));
                imgLuntan.setImageResource(R.drawable.luntan);
                tvLuntan.setTextColor(Color.parseColor("#bfbfbf"));
                imgMine.setImageResource(R.drawable.mine);
                tvMine.setTextColor(Color.parseColor("#bfbfbf"));
            }
        });
        llLuntan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(2);
                imgHome.setImageResource(R.drawable.home);
                tvHome.setTextColor(Color.parseColor("#bfbfbf"));
                imgFabu.setImageResource(R.drawable.fabu);
                tvFabu.setTextColor(Color.parseColor("#bfbfbf"));
                imgLuntan.setImageResource(R.drawable.xluntan);
                tvLuntan.setTextColor(Color.parseColor("#f5cc5b"));
                imgMine.setImageResource(R.drawable.mine);
                tvMine.setTextColor(Color.parseColor("#bfbfbf"));
            }
        });

        llMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(3);
                imgHome.setImageResource(R.drawable.home);
                tvHome.setTextColor(Color.parseColor("#bfbfbf"));
                imgFabu.setImageResource(R.drawable.fabu);
                tvFabu.setTextColor(Color.parseColor("#bfbfbf"));
                imgLuntan.setImageResource(R.drawable.luntan);
                tvLuntan.setTextColor(Color.parseColor("#bfbfbf"));
                imgMine.setImageResource(R.drawable.xmine);
                tvMine.setTextColor(Color.parseColor("#f5cc5b"));
            }
        });
    }

    //切换fg页面
    private void setFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new PublishFragment();
                break;
            case 2:
                fragment = new ForumFragment();
                break;
            case 3:
                fragment = new MineFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fr, fragment).commit();

    }


    private void initView() {
        fr = findViewById(R.id.fr);
        llHome = findViewById(R.id.ll_home);
        imgHome = findViewById(R.id.img_home);
        tvHome = findViewById(R.id.tv_home);
        llFabu = findViewById(R.id.ll_fabu);
        imgFabu = findViewById(R.id.img_fabu);
        tvFabu = findViewById(R.id.tv_fabu);
        llLuntan = findViewById(R.id.ll_luntan);
        imgLuntan = findViewById(R.id.img_luntan);
        tvLuntan = findViewById(R.id.tv_luntan);
        llMine = findViewById(R.id.ll_mine);
        imgMine = findViewById(R.id.img_mine);
        tvMine = findViewById(R.id.tv_mine);
    }
}