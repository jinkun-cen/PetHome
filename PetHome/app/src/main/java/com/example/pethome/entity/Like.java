package com.example.pethome.entity;

import com.database.lib.Column;

public class Like {

    @Column(primaryKey = true, notNull = true, order = 1)
    private Integer id;

    @Column(notNull = true, order = 2)
    private Integer userId;

    @Column(notNull = true, order = 3)
    private Integer articleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
