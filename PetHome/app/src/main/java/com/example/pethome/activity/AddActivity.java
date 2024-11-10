package com.example.pethome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.database.lib.SqlQueue;
import com.example.pethome.entity.Forum;
import com.example.pethome.sql.ForumHelper;
import com.example.pethome.R;
import com.example.pethome.entity.User;
import com.example.pethome.utils.CurrentUserUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
    private ImageView imgBack;
    private Button btnAdd;
    private EditText etContent;
    private ImageView imgAdd;

    private String base64Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add); // 设置当前界面的布局为 activity_add.xml
        initView(); // 初始化界面控件
        back(); // 设置返回按钮的点击事件
        add(); // 设置添加按钮的点击事件
        img(); // 设置添加图片按钮的点击事件
    }


    private void back() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 结束当前活动
            }
        });
    }


    private void img() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开系统相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE); // 启动选择图片的意图，并设置请求码
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // 获取选择的图片Uri
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // 将 Uri 转换为 Base64 编码的字符串
                base64Image = getBase64FromUri(selectedImageUri);
                // 更新 imgAdd 的显示
                imgAdd.setImageURI(selectedImageUri);
            }
        }
    }

    // 将 Uri 转换为 Base64 编码的字符串
    private String getBase64FromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void add() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入的文本内容
                String content = etContent.getText().toString().trim();

                // 检查输入的文本内容是否为空
                if (content.isEmpty()) {
                    Toast.makeText(AddActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查是否已上传图片
                if (imgAdd.getDrawable() == null) {
                    Toast.makeText(AddActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 获取当前北京时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
                String currentTime = sdf.format(new Date());

                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Boolean>() {
                    @Override
                    public Boolean execute() throws Exception {
                        // 获取用户信息
                        User user = CurrentUserUtils.get();
                        // 添加数据
                        Forum forum=new Forum();
                        forum.setUserId(user.getId());
                        forum.setContent(content);
                        forum.setImgContent(base64Image);
                        forum.setCreateTime(currentTime);
                        return ForumHelper.addData(forum);
                    }

                    @Override
                    public void onExecuteSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            finish(); // 结束当前活动
                        } else {
                            Toast.makeText(AddActivity.this, "添加失败！", Toast.LENGTH_SHORT).show(); // 弹出添加失败提示
                        }
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(AddActivity.this, s, Toast.LENGTH_SHORT).show(); // 弹出错误提示
                    }
                });
            }
        });
    }


    // 获取控件
    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etContent = findViewById(R.id.et_content);
        imgAdd = findViewById(R.id.img_add);
    }
}