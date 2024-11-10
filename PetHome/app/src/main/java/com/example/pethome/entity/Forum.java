package com.example.pethome.entity;

import com.database.lib.Column;

public class Forum {

    /**
     * 帖子id
     */
    @Column(primaryKey = true, notNull = true, order = 1)
    private Integer id;
    /**
     * 用户id
     */
    @Column(notNull = true, order = 2)
    private Integer userId;
    /**
     * 帖子内容
     */
    @Column(notNull = true, order = 3)
    private String content;
    /**
     * 图片内容
     */
    @Column(notNull = true, order = 4)
    private String imgContent;
    /**
     * 发布时间
     */
    @Column(notNull = true, order = 5)
    private String createTime;

    private User user;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgContent() {
        return imgContent;
    }

    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
