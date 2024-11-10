package com.example.pethome.fragment.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.database.lib.SqlQueue;
import com.example.pethome.entity.User;
import com.example.pethome.sql.UserHelper;
import com.example.pethome.MainActivity;
import com.example.pethome.R;
import com.example.pethome.utils.CurrentUserUtils;

public class LoginFragment extends Fragment {

    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        initView(v);
        login();
        return v;
    }

    private void login() {
        btnLogin.setOnClickListener(v -> {
            // 获取控件中的内容
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            // 输入内容不为空判断
            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "请输入手机号或密码！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<User>() {
                    @Override
                    public User execute() throws Exception {
                        boolean checked = UserHelper.checkUser(phone, password);
                        if (!checked) {
                            throw new Exception("用户名或密码错误！");
                        }
                        return UserHelper.getUserByPhone(phone);
                    }

                    @Override
                    public void onExecuteSuccess(User user) {
                        CurrentUserUtils.set(user);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        Toast.makeText(getActivity(), "登陆成功！", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 获取控件
    private void initView(View v) {
        etPhone = v.findViewById(R.id.et_phone);
        etPassword = v.findViewById(R.id.et_password);
        btnLogin = v.findViewById(R.id.btn_login);

    }
}