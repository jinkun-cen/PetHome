package com.example.pethome.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.database.lib.SqlQueue;
import com.example.pethome.sql.ArticleHelper;
import com.example.pethome.R;
import com.example.pethome.entity.Article;
import com.example.pethome.utils.CurrentUserUtils;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class PublishFragment extends Fragment {

    private ImageView imgPet;
    private RadioGroup rgType;
    private RadioButton rbLy;
    private RadioButton rbXc;
    private EditText etPetname;
    private EditText etPettype;
    private EditText etAge;
    private RadioGroup rgSex;
    private RadioButton rbG;
    private RadioButton rbM;
    private EditText etAddress;
    private EditText etColor;
    private EditText etPhone;
    private EditText etDetails;
    private Button btnFabu;
    private static final int REQUEST_IMAGE_SELECT = 1001;

    private String img;
    private Uri croppedUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fa_bu, container, false);
        initView(v);
        fabu();
        // 添加点击图片的监听器
        imgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(); // 打开相册以选择新的图片
            }
        });

        return v;
    }

    private void fabu() {
        btnFabu.setOnClickListener(v -> {
            // 获取所有输入字段的文本内容
            String petName = etPetname.getText().toString().trim();
            String petType = etPettype.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String color = etColor.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String details = etDetails.getText().toString().trim();
            // 检查是否上传了宠物图片
            if (croppedUri == null) {
                Toast.makeText(getActivity(), "请上传宠物图片!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // 获取头像并转换为 Base64
                BitmapDrawable drawable = (BitmapDrawable) imgPet.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                img = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }

            // 检查所有字段是否为空
            if (petName.isEmpty() || petType.isEmpty() || age.isEmpty() || address.isEmpty() ||
                    color.isEmpty() || phone.isEmpty() || details.isEmpty()) {
                // 如果有任何一个字段为空，显示提示消息
                Toast.makeText(getActivity(), "请填写所有信息", Toast.LENGTH_SHORT).show();
            } else {
                // 获取当前北京时间
                String time = getCurrentTimeInBeijing();
                // 如果所有字段都不为空，则执行发布操作
                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Boolean>() {
                    @Override
                    public Boolean execute() throws Exception {
                        // 获取用户选择的性别
                        String sex = "";
                        int selectedSexId = rgSex.getCheckedRadioButtonId();
                        if (selectedSexId == R.id.rb_g) {
                            sex = "公";
                        } else if (selectedSexId == R.id.rb_m) {
                            sex = "母";
                        }
                        // 获取用户选择的文章类型
                        String articleType = "";
                        int selectedPetTypeId = rgType.getCheckedRadioButtonId();
                        if (selectedPetTypeId == R.id.rb_ly) {
                            articleType = "领养";
                        } else if (selectedPetTypeId == R.id.rb_xc) {
                            articleType = "寻宠";
                        }
                        Article article = new Article();
                        article.setUserId(CurrentUserUtils.get().getId());
                        article.setPetImage(img);
                        article.setArticleType(articleType);
                        article.setPetName(petName);
                        article.setPetType(petType);
                        article.setPetAge(age);
                        article.setPetSex(sex);
                        article.setAddress(address);
                        article.setPetColor(color);
                        article.setPhone(phone);
                        article.setPetDetail(details);
                        article.setPublishTime(time);
                        article.setViews(0);
                        article.setLikes(0);
                        return ArticleHelper.addData(article);
                    }

                    @Override
                    public void onExecuteSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            // 发布成功，清空输入框内容
                            etPetname.setText("");
                            etPettype.setText("");
                            etAge.setText("");
                            etAddress.setText("");
                            etColor.setText("");
                            etPhone.setText("");
                            etDetails.setText("");
                            // 设置 RadioButton 选择
                            rbG.setChecked(true); // 设置为“公”
                            rbLy.setChecked(true); // 设置为“领养”
                            imgPet.setImageResource(R.drawable.upimg);
                            croppedUri = null;
                            // 提示发布成功
                            Toast.makeText(getActivity(), "发布成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "发布失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
                .withAspectRatio(800, 500) // 设置裁剪比例为500:150
                .start(requireContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            // 获取选择的图片，并裁剪
            startCropActivity(data.getData());
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            // 获取裁剪后的图片，并显示在 ImageView 中
            croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                // 在裁剪后的图片 URI 后添加时间戳或随机数
                // 这里使用时间戳
                Uri newUri = appendTimestampToUri(croppedUri);
                imgPet.setImageURI(newUri); // 将裁剪后的图片显示在 ImageView 中
            }
        }
    }

    // 在裁剪后的图片 URI 后添加时间戳
    private Uri appendTimestampToUri(Uri uri) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uriString = uri.toString();
        // 在 URI 后添加时间戳
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("timestamp", timestamp);
        return builder.build();
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
        imgPet = v.findViewById(R.id.img_pet);
        rgType = v.findViewById(R.id.rg_type);
        rbLy = v.findViewById(R.id.rb_ly);
        rbXc = v.findViewById(R.id.rb_xc);
        etPetname = v.findViewById(R.id.et_petname);
        etPettype = v.findViewById(R.id.et_pettype);
        etAge = v.findViewById(R.id.et_age);
        rgSex = v.findViewById(R.id.rg_sex);
        rbG = v.findViewById(R.id.rb_g);
        rbM = v.findViewById(R.id.rb_m);
        etAddress = v.findViewById(R.id.et_address);
        etColor = v.findViewById(R.id.et_color);
        etPhone = v.findViewById(R.id.et_phone);
        etDetails = v.findViewById(R.id.et_details);
        btnFabu = v.findViewById(R.id.btn_fabu);

    }
}