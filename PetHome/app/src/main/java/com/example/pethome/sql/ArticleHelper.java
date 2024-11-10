package com.example.pethome.sql;

import com.database.lib.Database;
import com.database.lib.where.Where;
import com.example.pethome.entity.Article;

import java.util.List;

public class ArticleHelper {

    /**
     * 添加新条目
     */
    public static boolean addData(Article article) throws Exception {
        long insert = Database.getTable(Article.class).insert(article);
        return insert > 0;
    }

    /**
     * 根据 articleType(文章类型) 查询所有数据
     */
    public static List<Article> getDataByArticleType(String articleType) throws Exception {
        List<Article> articleList= Database.getTable(Article.class)
                .where(Where.and("article_type", "=", articleType))
                .orderBy("id", "desc")
                .select();
        for (Article article : articleList) {
            article.setUser(UserHelper.getById(article.getUserId()));
            article.setReviews(ReviewHelper.getCountByArticleId(article.getId()));
        }
        return articleList;
    }

    /**
     * 查询所有数据
     */
    public static List<Article> getAllData() throws Exception {
        List<Article> articleList = Database.getTable(Article.class).orderBy("id", "desc").select();
        for (Article article : articleList) {
            article.setUser(UserHelper.getById(article.getUserId()));
            article.setReviews(ReviewHelper.getCountByArticleId(article.getId()));
        }
        return articleList;
    }

    /**
     * 根据id删除数据
     */
    public static void deleteById(Integer id) throws Exception {
        Database.getTable(Article.class).where(Where.and("id", "=", id)).delete();
    }

    /**
     * 更新浏览量
     */
    public static void updateViewsById(Integer id, int views) throws Exception {
        Article article = new Article();
        article.setViews(views);
        Database.getTable(Article.class).where(Where.and("id", "=", id)).update(article);
    }

    /**
     * 更新 likes
     */
    public static void updateLikesById(Integer id, int likes) throws Exception {
        Article article = new Article();
        article.setLikes(likes);
        Database.getTable(Article.class).where(Where.and("id", "=", id)).update(article);
    }

    /**
     * 根据时间查询单条数据
     */
    public static Article getById(Integer id) throws Exception {
        return Database.getTable(Article.class).where(Where.and("id", "=", id)).selectOne();
    }

    /**
     * 根据用户id删除用户信息
     */
    public static void deleteByUserId(Integer userId) throws Exception {
        Database.getTable(Article.class).where(Where.and("user_id", "=", userId)).delete();
    }

}
