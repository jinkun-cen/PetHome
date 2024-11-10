package com.example.pethome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.database.lib.SqlQueue;
import com.example.pethome.adapter.ReviewAdapter;
import com.example.pethome.R;
import com.example.pethome.entity.Article;
import com.example.pethome.entity.Review;
import com.example.pethome.entity.User;
import com.example.pethome.sql.ArticleHelper;
import com.example.pethome.sql.LikeHelper;
import com.example.pethome.sql.ReviewHelper;
import com.example.pethome.utils.CurrentUserUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgPet;
    private TextView tvPetName;
    private ImageView imgPetSex;
    private ImageView imgLikes;
    private TextView tvPetType;
    private TextView tvColor;
    private TextView tvArticleType;
    private TextView tvDetails;
    private TextView tvAddress;
    private ImageView imgUser;
    private TextView tvUsername;
    private TextView tvUserphone;

    private Integer articleId;

    private RecyclerView rv;
    private EditText etReview;
    private Button btnReview;

    private TextView tvAge;

    private Boolean isLike = false;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        currentUser = CurrentUserUtils.get();
        initView();
        show();
        back();
        showreview();
        review();
        likes();
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

    private void review() {
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的评论内容
                String content = etReview.getText().toString();
                // 获取当前北京时间
                String reviewTime = getCurrentTimeInBeijing();
                // 检查评论内容是否不为空
                if (!TextUtils.isEmpty(content)) {
                    SqlQueue.submit(getLifecycle(), new SqlQueue.Task<List<Review>>() {
                        @Override
                        public List<Review> execute() throws Exception {
                            // 尝试向数据库中添加评论
                            Review review = new Review();
                            review.setArticleId(articleId);
                            review.setUserId(currentUser.getId());
                            review.setContent(content);
                            review.setReviewTime(reviewTime);
                            ReviewHelper.addReview(review);
                            return ReviewHelper.getListByArticleId(articleId);
                        }

                        @Override
                        public void onExecuteSuccess(List<Review> reviewList) {
                            // 根据添加结果进行操作
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv.setLayoutManager(layoutManager);
                            ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), reviewList);
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            // 显示发布成功提示，并清空评论输入框
                            Toast.makeText(ArticleDetailsActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                            etReview.setText("");
                        }

                        @Override
                        public void onExecuteError(String s) {
                            // 若发生错误，则显示错误信息
                            Toast.makeText(ArticleDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    // 若评论内容为空，则显示请输入评论提示
                    Toast.makeText(ArticleDetailsActivity.this, "请输入评论！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void likes() {
        // 查询指定id和用户名的收藏状态
        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                return LikeHelper.isLike(currentUser.getId(), articleId);
            }

            @Override
            public void onExecuteSuccess(Boolean aBoolean) {
                isLike = aBoolean;
                if (isLike) {
                    imgLikes.setImageResource(R.drawable.xdz);
                } else {
                    imgLikes.setImageResource(R.drawable.dz);
                }
            }

            @Override
            public void onExecuteError(String s) {
                Toast.makeText(ArticleDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置点赞按钮的点击事件监听器
        imgLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Void>() {
                    @Override
                    public Void execute() throws Exception {
                        Article article = ArticleHelper.getById(articleId);
                        if (article == null) {
                            throw new Exception("文章不存在");
                        }
                        if (isLike) {
                            LikeHelper.unlike(currentUser.getId(), articleId);
                            // 更新点赞量,减一
                            ArticleHelper.updateLikesById(articleId, article.getLikes() - 1);
                        } else {
                            LikeHelper.like(currentUser.getId(), articleId);
                            // 更新点赞量,加一
                            ArticleHelper.updateLikesById(articleId, article.getLikes() + 1);
                        }
                        return null;
                    }

                    @Override
                    public void onExecuteSuccess(Void unused) {
                        if (isLike) {
                            imgLikes.setImageResource(R.drawable.dz);
                            isLike = false;
                        } else {
                            imgLikes.setImageResource(R.drawable.xdz);
                            isLike = true;
                        }
                    }

                    @Override
                    public void onExecuteError(String s) {
                        Toast.makeText(ArticleDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 显示所有评论
    private void showreview() {
        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<List<Review>>() {
            @Override
            public List<Review> execute() throws Exception {
                return ReviewHelper.getListByArticleId(articleId);
            }

            @Override
            public void onExecuteSuccess(List<Review> reviews) {
                if (!reviews.isEmpty()) {
                    // 若评论列表不为空，显示评论信息
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ArticleDetailsActivity.this);
                    rv.setLayoutManager(layoutManager);
                    ReviewAdapter adapter = new ReviewAdapter(ArticleDetailsActivity.this, reviews);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onExecuteError(String s) {

            }
        });
    }

    private void back() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // 显示文章详情信息
    private void show() {
        SqlQueue.submit(getLifecycle(), new SqlQueue.Task<Article>() {
            @Override
            public Article execute() throws Exception {
                Article article = ArticleHelper.getById(articleId);
                ArticleHelper.updateViewsById(articleId, article.getViews() + 1);
                return article;
            }

            @Override
            public void onExecuteSuccess(Article article) {
                Glide.with(ArticleDetailsActivity.this)
                        .load(Base64.decode(article.getPetImage(), Base64.DEFAULT))
                        .into(imgPet);
                tvPetName.setText(article.getPetName());
                if (article.getPetSex().equals("公")) {
                    imgPetSex.setImageResource(R.drawable.g);
                } else {
                    imgPetSex.setImageResource(R.drawable.m);
                }
                tvPetType.setText(article.getPetType());
                tvColor.setText(article.getPetColor());
                tvArticleType.setText(article.getArticleType());
                tvDetails.setText("    " + article.getPetDetail());
                tvAddress.setText(article.getAddress());
                tvAge.setText(article.getPetAge());
                Glide.with(ArticleDetailsActivity.this)
                        .load(Base64.decode(currentUser.getAvatar(), Base64.DEFAULT))
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imgUser);
                tvUsername.setText(currentUser.getUsername());
                tvUserphone.setText(currentUser.getPhone());
            }

            @Override
            public void onExecuteError(String s) {
                Toast.makeText(ArticleDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 获取控件
    private void initView() {
        articleId = getIntent().getIntExtra("articleId", 0);

        imgBack = findViewById(R.id.img_back);
        imgPet = findViewById(R.id.img_pet);
        tvPetName = findViewById(R.id.tv_petName);
        imgPetSex = findViewById(R.id.img_petSex);
        imgLikes = findViewById(R.id.img_Likes);
        tvPetType = findViewById(R.id.tv_petType);
        tvColor = findViewById(R.id.tv_color);
        tvArticleType = findViewById(R.id.tv_articleType);
        tvDetails = findViewById(R.id.tv_details);
        tvAddress = findViewById(R.id.tv_address);
        imgUser = findViewById(R.id.img_user);
        tvUsername = findViewById(R.id.tv_username);
        tvUserphone = findViewById(R.id.tv_userphone);

        rv = findViewById(R.id.rv);
        etReview = findViewById(R.id.et_review);
        btnReview = findViewById(R.id.btn_review);

        tvAge = findViewById(R.id.tv_age);
    }
}