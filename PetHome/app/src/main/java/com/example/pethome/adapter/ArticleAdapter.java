package com.example.pethome.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.pethome.R;
import com.example.pethome.activity.ArticleDetailsActivity;
import com.example.pethome.entity.Article;
import com.example.pethome.entity.User;
import com.example.pethome.utils.CurrentUserUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends BaseAdapter {

    private final List<Article> dataList = new ArrayList<>(); // 数据列表

    private String avatar;

    private String username;

    private OnItemClickListener onItemClickListener;

    // 获取列表项数量
    @Override
    public int getCount() {
        return dataList.size();
    }

    // 获取指定位置的列表项
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    // 获取指定位置的列表项ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 获取列表项的视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Context context = parent.getContext();
        if (convertView == null) { // 如果视图为空，则进行初始化
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgUser = convertView.findViewById(R.id.img_user);
            viewHolder.tvUsername = convertView.findViewById(R.id.tv_username);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            viewHolder.btnArticleType = convertView.findViewById(R.id.btn_articleType);
            viewHolder.btnPettype = convertView.findViewById(R.id.btn_pettype);
            viewHolder.btnAge = convertView.findViewById(R.id.btn_age);
            viewHolder.btnPetcolor = convertView.findViewById(R.id.btn_petcolor);
            viewHolder.tvPetname = convertView.findViewById(R.id.tv_petname);
            viewHolder.imgPet = convertView.findViewById(R.id.img_pet);
            viewHolder.tvDianzan = convertView.findViewById(R.id.tv_dianzan);
            viewHolder.tvPinglun = convertView.findViewById(R.id.tv_pinglun);
            viewHolder.tvLiulanliang = convertView.findViewById(R.id.tv_liulanliang);
            viewHolder.imgDelete = convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取当前位置的动态数据
        Article item = dataList.get(position);
        // 更新 Likes 和 Views 的值
        viewHolder.tvDianzan.setText(String.valueOf(item.getLikes()));
        viewHolder.tvLiulanliang.setText(String.valueOf(item.getViews()));

        User userByPhone = item.getUser();
        if (userByPhone != null) {
            avatar = userByPhone.getAvatar();
            username = userByPhone.getUsername();
        }
        Glide.with(context)
                .load(Base64.decode(avatar, Base64.DEFAULT))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(viewHolder.imgUser);
        viewHolder.tvUsername.setText(username); // 设置用户名
        viewHolder.tvTime.setText(item.getPublishTime()); // 设置时间
        viewHolder.btnArticleType.setText(item.getArticleType()); // 设置文章类型
        viewHolder.btnPettype.setText(item.getPetType()); // 设置宠物类型
        viewHolder.btnAge.setText(item.getPetAge());
        viewHolder.btnPetcolor.setText(item.getPetColor());
        viewHolder.tvPetname.setText(item.getPetName());
        Glide.with(context)
                .load(Base64.decode(item.getPetImage(), Base64.DEFAULT))
                .into(viewHolder.imgPet);
        // 如果当前用户为发布者，显示删除按钮并设置点击事件
        if (item.getUserId().equals(CurrentUserUtils.get().getId())) {
            viewHolder.imgDelete.setVisibility(View.VISIBLE); // 显示删除按钮
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出确认对话框，确认是否删除该动态
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("确定要删除吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (onItemClickListener != null) {
                                        onItemClickListener.onItemDeleteClick(position, item);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 用户取消删除操作，对话框消失
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show(); // 显示对话框
                }
            });

        } else {
            viewHolder.imgDelete.setVisibility(View.GONE); // 隐藏删除按钮
        }
        viewHolder.tvPinglun.setText(String.valueOf(item.getReviews()));
        // 为列表项设置点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 Intent 启动 MainActivity
                Intent intent = new Intent(context, ArticleDetailsActivity.class);
                intent.putExtra("articleId", item.getId());
                context.startActivity(intent);
            }
        });

        return convertView; // 返回视图
    }

    public void setData(List<Article> articles) {
        dataList.clear();
        dataList.addAll(articles);
        notifyDataSetChanged();
    }

    public void removeData(Article article) {
        dataList.remove(article);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemDeleteClick(int position, Article article);
    }

    // ViewHolder 静态内部类，用于缓存列表项视图中的子视图对象
    static class ViewHolder {
        ImageView imgUser;
        TextView tvUsername;
        TextView tvTime;
        Button btnArticleType;
        Button btnPettype;
        Button btnAge;
        Button btnPetcolor;
        TextView tvPetname;
        ImageView imgPet;
        TextView tvDianzan;
        TextView tvPinglun;
        TextView tvLiulanliang;
        ImageView imgDelete;
    }
}
