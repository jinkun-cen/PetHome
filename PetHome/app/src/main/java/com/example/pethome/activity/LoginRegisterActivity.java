package com.example.pethome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pethome.fragment.LoginRegister.LoginFragment;
import com.example.pethome.fragment.LoginRegister.RegisterFragment;
import com.example.pethome.R;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        initView();
        Navigation();
        btnLogin.callOnClick();//默认显示注册
    }

    // 点击控件进行页面转换
    private void Navigation() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(0);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(1);
            }
        });
    }

    //切换fg页面
    private void setFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case 0:
                fragment = new LoginFragment();
                break;
            case 1:
                fragment = new RegisterFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fr, fragment).commit();
    }

    // 获取控件
    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }
}