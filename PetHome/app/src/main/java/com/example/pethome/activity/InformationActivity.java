package com.example.pethome.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.pethome.R;
import com.example.pethome.entity.User;
import com.example.pethome.utils.CurrentUserUtils;

public class InformationActivity extends AppCompatActivity {

    private ImageView imgUser;
    private TextView tvUsername;
    private TextView tvuserPhone;
    private TextView tvPassword;
    private TextView tvTime;

    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
        back();
        show();
    }

    private void back() {
        // 设置返回按钮的点击监听器
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 结束当前活动
            }
        });
    }

    private void show() {
        User user = CurrentUserUtils.get();
        // 加载用户头像并显示
        Glide.with(InformationActivity.this)
                .load(Base64.decode(user.getAvatar(), Base64.DEFAULT))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imgUser);
        // 显示用户密码
        tvPassword.setText(user.getPassword());
        // 显示用户注册时间
        tvTime.setText(user.getCreateTime());
        // 显示用户名
        tvUsername.setText(user.getUsername());
        // 显示用户手机号
        tvuserPhone.setText(user.getPhone());
    }

    private void initView() {
        // 初始化视图组件
        imgUser = findViewById(R.id.img_user);
        tvUsername = findViewById(R.id.tv_username);
        tvuserPhone = findViewById(R.id.tvuserPhone);
        tvPassword = findViewById(R.id.tv_password);
        tvTime = findViewById(R.id.tv_time);
        // 获取返回按钮
        imgBack = findViewById(R.id.img_back);
    }

}