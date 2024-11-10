package com.example.pethome.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.database.lib.SqlQueue;
import com.example.pethome.MainActivity;
import com.example.pethome.R;
import com.example.pethome.activity.InformationActivity;
import com.example.pethome.activity.LoginRegisterActivity;
import com.example.pethome.entity.User;
import com.example.pethome.sql.ArticleHelper;
import com.example.pethome.sql.ForumHelper;
import com.example.pethome.sql.LikeHelper;
import com.example.pethome.sql.UserHelper;
import com.example.pethome.utils.CurrentUserUtils;

public class MineFragment extends Fragment implements View.OnClickListener {

    private ImageView imgUser;
    private TextView tvUsername;
    private TextView tvuserPhone;
    private LinearLayout llInformation;
    private LinearLayout llPassword;
    private LinearLayout llfb;
    private LinearLayout lllt;
    private LinearLayout llZhuxiao;
    private LinearLayout llQuit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(v);
        show();
        return v;
    }

    // 显示用户信息
    private void show() {
        User user = CurrentUserUtils.get();
        Glide.with(getActivity())
                .load(Base64.decode(user.getAvatar(), Base64.DEFAULT))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imgUser);
        tvUsername.setText(user.getUsername());
        tvuserPhone.setText(user.getPhone());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_information) {// 点击个人信息
            Intent information = new Intent(getActivity(), InformationActivity.class);
            startActivity(information);
        } else if (id == R.id.ll_password) {// 创建一个 AlertDialog.Builder 对象
            AlertDialog.Builder modify = new AlertDialog.Builder(getActivity());
            // 设置自定义布局
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_password, null);
            modify.setView(dialogView);

            // 获取自定义布局中的控件
            TextView tvUserName = dialogView.findViewById(R.id.tv_username);
            TextView tvUserPhone = dialogView.findViewById(R.id.tv_userphone);
            EditText etOldPassword = dialogView.findViewById(R.id.et_oldpassword);
            EditText etNewPassword = dialogView.findViewById(R.id.et_newPassword);
            Button btnUpdatePassword = dialogView.findViewById(R.id.button);
            TextView tvCancel = dialogView.findViewById(R.id.tv_quxiao);
            User user = CurrentUserUtils.get();
            tvUserName.setText(user.getUsername());
            tvUserPhone.setText(user.getPhone());

            // 创建对话框
            AlertDialog alertDialog = modify.create();

            // 点击“立即修改”按钮
            btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 获取输入的原密码和新密码
                    String oldPassword = etOldPassword.getText().toString().trim();
                    String newPassword = etNewPassword.getText().toString().trim();
                    if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                        // 在这里执行修改密码的逻辑，可以调用相应的方法或发送请求
                        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Boolean>() {
                            @Override
                            public Boolean execute() throws Exception {
                                return UserHelper.updateUserPassword(user.getPhone(), oldPassword, newPassword);
                            }

                            @Override
                            public void onExecuteSuccess(Boolean aBoolean) {
                                if (aBoolean) {
                                    Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                                    Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "修改失败！原密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onExecuteError(String s) {
                                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "旧密码和新密码不能为空！", Toast.LENGTH_SHORT).show();
                    }

                    // 关闭对话框
                    alertDialog.dismiss();
                }
            });

            // 点击“取消修改”按钮
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 关闭对话框
                    alertDialog.dismiss();
                }
            });

            // 显示对话框
            alertDialog.show();
        } else if (id == R.id.ll_fb) {// 点击发布
            Intent fb = new Intent(getActivity(), MainActivity.class);
            fb.putExtra("mine", "fb");
            startActivity(fb);
            getActivity().finish();
        } else if (id == R.id.ll_lt) {// 点击论坛
            Intent lt = new Intent(getActivity(), MainActivity.class);
            lt.putExtra("mine", "lt");
            startActivity(lt);
            getActivity().finish();
        } else if (id == R.id.ll_zhuxiao) {// 点击注销账号
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("确认注销账号");
            builder.setMessage("您确定要注销当前账号吗？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 执行注销账号的操作
                    SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Void>() {
                        @Override
                        public Void execute() throws Exception {
                            Integer userId = CurrentUserUtils.get().getId();
                            UserHelper.deleteById(userId);
                            ArticleHelper.deleteByUserId(userId);
                            LikeHelper.deleteByUserId(userId);
                            ForumHelper.deleteByUserId(userId);
                            return null;
                        }

                        @Override
                        public void onExecuteSuccess(Void unused) {
                            Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
                            getActivity().finishAffinity();
                        }

                        @Override
                        public void onExecuteError(String s) {
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 取消注销账号，关闭对话框
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (id == R.id.ll_quit) {// 点击退出应用
            AlertDialog.Builder tuichu = new AlertDialog.Builder(getActivity());
            tuichu.setTitle("确认退出");
            tuichu.setMessage("您确定要退出登录吗？");
            tuichu.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 退出应用
                    Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                    Toast.makeText(getActivity(), "退出成功！", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            tuichu.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 取消退出应用，关闭对话框
                    dialog.dismiss();
                }
            });
            tuichu.show();
        }
    }

    private void initView(View v) {
        imgUser = v.findViewById(R.id.img_user);
        tvUsername = v.findViewById(R.id.tv_username);
        tvuserPhone = v.findViewById(R.id.tvuserPhone);

        llInformation = v.findViewById(R.id.ll_information);
        llPassword = v.findViewById(R.id.ll_password);
        llfb = v.findViewById(R.id.ll_fb);
        lllt = v.findViewById(R.id.ll_lt);
        llZhuxiao = v.findViewById(R.id.ll_zhuxiao);
        llQuit = v.findViewById(R.id.ll_quit);

        llInformation.setOnClickListener(this);
        llPassword.setOnClickListener(this);
        llfb.setOnClickListener(this);
        lllt.setOnClickListener(this);
        llZhuxiao.setOnClickListener(this);
        llQuit.setOnClickListener(this);

    }
}