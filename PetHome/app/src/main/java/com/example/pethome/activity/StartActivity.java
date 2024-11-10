package com.example.pethome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.database.lib.Database;
import com.database.lib.DatabaseConfig;
import com.database.lib.DatabaseType;
import com.example.pethome.R;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {
    TimeCount timeCount;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tomainActive();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        DatabaseConfig config = new DatabaseConfig();
        config.setDatabaseType(DatabaseType.SQLITE);
        config.setDatabase("pethome");
        Database.setConfig(config);

        // 1.延迟二秒执行 runnable
        handler.postDelayed(runnable, 2000);
        // 2.初始化,共执行3秒,一秒执行一次
        timeCount = new TimeCount(3000, 1000);
        timeCount.start();
    }

    // 进入登陆页面
    private void tomainActive() {
        startActivity(new Intent(this, LoginRegisterActivity.class));
        // 跳转完成后注销
        finish();
    }
    // 计时器
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long l) {
        }
        @Override
        public void onFinish() {
            // 结束之后移除runnable(进入主页)
            handler.removeCallbacks(runnable);
        }
    }
}
