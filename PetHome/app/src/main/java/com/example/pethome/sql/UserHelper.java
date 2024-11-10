package com.example.pethome.sql;

import com.database.lib.Database;
import com.database.lib.where.Where;
import com.example.pethome.entity.User;

public class UserHelper {

    /**
     * 添加用户信息到数据库
     */
    public static void addUser(User user) throws Exception {
        Database.getTable(User.class).insert(user);
    }

    /**
     * 检查手机号和密码是否匹配
     */
    public static boolean checkUser(String phone, String password) throws Exception {
        User user = Database.getTable(User.class)
                .where(Where.and("phone", "=", phone))
                .where(Where.and("password", "=", password))
                .selectOne();
        return user != null;
    }

    /**
     * 检查手机号是否存在
     */
    public static boolean checkPhoneExist(String phone) throws Exception {
        User user = Database.getTable(User.class)
                .where(Where.and("phone", "=", phone))
                .selectOne();
        return user != null;
    }

    /**
     * 根据手机号查询用户信息
     */
    public static User getUserByPhone(String phone) throws Exception {
        return Database.getTable(User.class)
                .where(Where.and("phone", "=", phone))
                .selectOne();
    }

    /**
     * 根据id删除用户信息
     */
    public static void deleteById(Integer userId) throws Exception {
        Database.getTable(User.class)
                .where(Where.and("id", "=", userId))
                .delete();
    }

    /**
     * 更新用户密码
     */
    public static boolean updateUserPassword(String phone, String oldPassword, String newPassword) throws Exception {
        boolean isUserExist = checkUser(phone, oldPassword);
        if (!isUserExist) {
            throw new Exception("用户不存在或密码错误");
        }
        User user = new User();
        user.setPassword(newPassword);
        Database.getTable(User.class)
                .where(Where.and("phone", "=", phone))
                .where(Where.and("password", "=", oldPassword))
                .update(user);
        return true;
    }

    public static User getById(Integer userId) throws Exception {
        return Database.getTable(User.class)
                .where(Where.and("id", "=", userId))
                .selectOne();
    }
}
