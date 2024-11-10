package com.example.pethome.fragment.LoginRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.database.lib.SqlQueue;
import com.example.pethome.activity.LoginRegisterActivity;
import com.example.pethome.sql.UserHelper;
import com.example.pethome.R;
import com.example.pethome.entity.User;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class RegisterFragment extends Fragment {
    private static final int REQUEST_IMAGE_SELECT = 1001;
    private EditText etUsername;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etRepassword;
    private Button btnRegister;
    private ImageView imgCamera;
    private ImageView imgAvatar;
    private String avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        initView(v);
        register();
        logo();
        return v;
    }

    private void logo() {
        imgCamera.setOnClickListener(v -> {
            openGallery();
        });
    }

    // 打开相册
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }


    private void startCropActivity(Uri uri) {
        UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), "cropped_image")))
                .withAspectRatio(1, 1)
                .start(requireContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            // 获取选择的图片，并裁剪为正方形
            startCropActivity(data.getData());
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            // 获取裁剪后的图片，并显示在imgTouxiang上
            Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                imgAvatar.setImageURI(croppedUri);
                // 隐藏imgCamera
                imgCamera.setVisibility(View.GONE);
                imgAvatar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void register() {
        btnRegister.setOnClickListener(v -> {
            // 获取控件中的内容
            String username = etUsername.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            String repassword = etRepassword.getText().toString();
            // 获取当前北京时间
            String time = getCurrentTimeInBeijing();
            // 判断所有数据是否不为空
            if (username.isEmpty() || phone.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                Toast.makeText(getActivity(), "请输入完整信息！", Toast.LENGTH_SHORT).show();
                return;
            }

            // 判断两次密码是否一致
            if (!password.equals(repassword)) {
                Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                return;
            }
            // 检查是否上传了头像
            if (imgAvatar.getVisibility() == View.GONE) {
                Toast.makeText(getActivity(), "请上传头像!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // 获取头像并转换为 Base64
                BitmapDrawable drawable = (BitmapDrawable) imgAvatar.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                avatar = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Void>() {
                @Override
                public Void execute() throws Exception {
                    boolean checked = UserHelper.checkUser(phone, password);
                    if (checked) {
                        throw new Exception("手机号已存在！");
                    }
                    User user = new User();
                    user.setUsername(username);
                    user.setPhone(phone);
                    user.setPassword(password);
                    user.setCreateTime(time);
                    user.setAvatar(avatar);
                    UserHelper.addUser(user);
                    return null;
                }

                @Override
                public void onExecuteSuccess(Void unused) {
                    Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                    Toast.makeText(getActivity(), "注册成功！", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onExecuteError(String s) {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // 获取当前北京时间
    private String getCurrentTimeInBeijing() {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 设置时区为北京时间
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(timeZone);
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    private void initView(View v) {
        etUsername = v.findViewById(R.id.et_username);
        etPhone = v.findViewById(R.id.et_phone);
        etPassword = v.findViewById(R.id.et_password);
        etRepassword = v.findViewById(R.id.et_repassword);
        btnRegister = v.findViewById(R.id.btn_register);
        imgCamera = v.findViewById(R.id.img_camera);
        imgAvatar = v.findViewById(R.id.img_avatar);
    }
}