package com.example.pethome.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.pethome.entity.Forum;
import com.example.pethome.entity.User;
import com.example.pethome.R;
import com.example.pethome.utils.CurrentUserUtils;

import java.util.ArrayList;
import java.util.List;

public class ForumAdapter extends BaseAdapter {

    /**
     * 数据列表
     */
    private List<Forum> dataList=new ArrayList<>();

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
        ViewHolder holder;
        Context context = parent.getContext(); // 获取上下文对象
        if (convertView == null) { // 如果视图为空，则进行初始化
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lt, parent, false);
            holder = new ViewHolder();
            holder.tvUsername = convertView.findViewById(R.id.tv_username);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            holder.imgContent = convertView.findViewById(R.id.img_content);
            holder.img = convertView.findViewById(R.id.img);
            holder.imgDelete = convertView.findViewById(R.id.img_delete);
            holder.tvTime = convertView.findViewById(R.id.tv_time);

            // 获取当前用户的手机号
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前位置的动态数据
        Forum item = dataList.get(position);
        User user=item.getUser();
        holder.tvUsername.setText(user.getUsername()); // 设置用户名
        holder.tvContent.setText(item.getContent()); // 设置内容
        holder.tvTime.setText(item.getCreateTime()); // 设置时间

        // 使用 Glide 加载图片内容，将 Base64 编码的字符串解码并显示到对应的 ImageView 上
        Glide.with(context)
                .load(Base64.decode(item.getImgContent(), Base64.DEFAULT))
                .into(holder.imgContent);

        Glide.with(context)
                .load(Base64.decode(user.getAvatar(), Base64.DEFAULT))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.img);

        // 如果当前用户为该动态的发布者，显示删除按钮并设置点击事件
        if (user.getPhone().equals(CurrentUserUtils.get().getPhone())) {
            holder.imgDelete.setVisibility(View.VISIBLE); // 显示删除按钮
            holder.imgDelete.setOnClickListener(v -> {
                // 弹出确认对话框，确认是否删除
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
            });

        } else {
            holder.imgDelete.setVisibility(View.GONE); // 隐藏删除按钮
        }
        return convertView; // 返回视图
    }

    public void removeData(Forum forum) {
        dataList.remove(forum);
        notifyDataSetChanged();
    }

    public void setData(List<Forum> forums) {
        dataList.clear();
        dataList.addAll(forums);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemDeleteClick(int position, Forum forum);
    }

    // ViewHolder 静态内部类，用于缓存列表项视图中的子视图对象
    static class ViewHolder {
        TextView tvUsername; // 用户名
        TextView tvContent; // 内容
        ImageView imgContent, img, imgDelete; // 内容图片、用户头像、删除按钮
        TextView tvTime; // 时间
    }
}
