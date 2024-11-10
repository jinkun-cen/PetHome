package com.example.pethome.sql;

import com.database.lib.Database;
import com.database.lib.where.Where;
import com.example.pethome.entity.Like;

public class LikeHelper {

    public static boolean isLike(Integer userId, Integer articleId) throws Exception {
        return Database.getTable(Like.class)
                .where(Where.and("user_id", "=", userId))
                .where(Where.and("article_id", "=", articleId))
                .selectOne() != null;
    }

    public static void like(Integer userId, Integer articleId) throws Exception {
        Like like = new Like();
        like.setUserId(userId);
        like.setArticleId(articleId);
        Database.getTable(Like.class).insert(like);
    }

    public static void unlike(Integer userId, Integer articleId) throws Exception {
        Database.getTable(Like.class)
                .where(Where.and("user_id", "=", userId))
                .where(Where.and("article_id", "=", articleId))
                .delete();
    }

    public static void deleteByUserId(Integer userId) throws Exception {
        Database.getTable(Like.class)
                .where(Where.and("user_id", "=", userId))
                .delete();
    }
}
