package com.example.pethome.sql;

import com.database.lib.Database;
import com.database.lib.where.Where;
import com.example.pethome.entity.Review;
import com.example.pethome.entity.User;

import java.util.List;

public class ReviewHelper {

    /**
     * 添加数据并返回是否添加成功
     */
    public static boolean addReview(Review review) throws Exception {
        return Database.getTable(Review.class).insert(review) > 0;
    }

    /**
     * 根据文章id查询评论数据
     */
    public static List<Review> getListByArticleId(Integer articleId) throws Exception {
        List<Review> reviews = Database.getTable(Review.class)
                .where(Where.and("article_id", "=", articleId))
                .select();
        for (Review review : reviews) {
            review.setUser(Database.getTable(User.class)
                    .where(Where.and("id", "=", review.getUserId()))
                    .selectOne());
        }
        return reviews;
    }

    /**
     * 根据评论id获取评论数量
     */
    public static int getCountByArticleId(Integer articleId) throws Exception {
        return Database.getTable(Review.class)
                .where(Where.and("article_id", "=", articleId))
                .select().size();
    }
}
