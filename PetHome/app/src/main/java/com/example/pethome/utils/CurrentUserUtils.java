package com.example.pethome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pethome.entity.User;

public class CurrentUserUtils {

    private static final String CURRENT_USER = "CURRENT_USER";

    private final SharedPreferences sp;

    public CurrentUserUtils() {
        sp = AppUtils.getApplication().getSharedPreferences(CURRENT_USER, Context.MODE_PRIVATE);
    }

    /**
     * 创建并获取单例
     */
    public static CurrentUserUtils getInstance() {
        return InstanceHolder.instance;
    }

    public static boolean isLogin() {
        return getInstance().sp.getInt("id", 0) != 0;
    }

    public static User get() {
        int id = getInstance().sp.getInt("id", 0);
        String username = getInstance().sp.getString("username", "");
        String password = getInstance().sp.getString("password", "");
        String phone = getInstance().sp.getString("phone", "");
        String avatar = getInstance().sp.getString("avatar", "");
        String createTime = getInstance().sp.getString("createTime", "");
        if (id == 0) {
            throw new RuntimeException("用户未登录");
        }
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setAvatar(avatar);
        user.setCreateTime(createTime);
        return user;
    }

    public static void set(User user) {
        SharedPreferences.Editor editor = getInstance().sp.edit();
        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("phone", user.getPhone());
        editor.putString("avatar", user.getAvatar());
        editor.putString("createTime", user.getCreateTime());
        editor.apply();
    }

    public static void clear() {
        SharedPreferences.Editor editor = getInstance().sp.edit();
        editor.clear();
        editor.apply();
    }

    private static final class InstanceHolder {
        /**
         * 单例
         */
        static final CurrentUserUtils instance = new CurrentUserUtils();
    }
}
