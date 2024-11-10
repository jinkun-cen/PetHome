package com.example.pethome.sql;

import com.database.lib.Database;
import com.database.lib.where.Where;
import com.example.pethome.entity.Forum;

import java.util.List;

public class ForumHelper {

    /**
     * 添加数据的方法
     */
    public static boolean addData(Forum forum) throws Exception {
        return Database.getTable(Forum.class).insert(forum) > 0;
    }

    /**
     * 查询所有数据的方法
     */
    public static List<Forum> getAllData() throws Exception {
        List<Forum> list = Database.getTable(Forum.class).orderBy("id","desc").select();
        for (Forum forum : list) {
            forum.setUser(UserHelper.getById(forum.getUserId()));
        }
        return list;
    }

    /**
     * 根据用户id删除数据的方法
     */
    public static void deleteById(Integer id) throws Exception {
        Database.getTable(Forum.class).where(Where.and("id", "=", id)).delete();
    }

    /**
     * 根据用户id删除数据的方法
     */
    public static void deleteByUserId(Integer userId) throws Exception {
        Database.getTable(Forum.class)
                .where(Where.and("user_id", "=", userId))
                .delete();
    }

}
