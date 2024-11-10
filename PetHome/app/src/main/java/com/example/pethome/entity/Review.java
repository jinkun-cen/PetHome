package com.example.pethome.entity;

import com.database.lib.Column;

public class Review {

    /**
     * 评论id
     */
    @Column(primaryKey = true, notNull = true, order = 1)
    private Integer id;
    /**
     * 文章id
     */
    @Column(notNull = true, order = 2)
    private Integer articleId;
    /**
     * 用户id
     */
    @Column(notNull = true, order = 3)
    private Integer userId;
    /**
     * 评论内容
     */
    @Column(notNull = true, order = 4)
    private String content;
    /**
     * 评论时间
     */
    @Column(notNull = true, order = 5)
    private String reviewTime;

    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}