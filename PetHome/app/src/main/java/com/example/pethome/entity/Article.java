package com.example.pethome.entity;

import com.database.lib.Column;

public class Article {
    /**
     * 文章id
     */
    @Column(primaryKey = true, notNull = true, order = 1)
    private Integer id;
    /**
     * 用户id
     */
    @Column(notNull = true, order = 2)
    private Integer userId;
    /**
     * 宠物图片
     */
    @Column(notNull = true, order = 3)
    private String petImage;
    /**
     * 文章类型：送养、领养
     */
    @Column(notNull = true, order = 4)
    private String articleType;
    /**
     * 宠物名字
     */
    @Column(notNull = true, length = 50, order = 5)
    private String petName;
    /**
     * 宠物类型
     */
    @Column(notNull = true, length = 50, order = 6)
    private String petType;
    /**
     * 宠物年龄
     */
    @Column(notNull = true, length = 20, order = 7)
    private String petAge;
    /**
     * 宠物性别
     */
    @Column(notNull = true, length = 10, order = 8)
    private String petSex;
    /**
     * 宠物地址
     */
    @Column(notNull = true, length = 100, order = 9)
    private String address;
    /**
     * 宠物颜色
     */
    @Column(notNull = true, length = 50, order = 10)
    private String petColor;
    /**
     * 联系电话
     */
    @Column(notNull = true, length = 20, order = 11)
    private String phone;
    /**
     * 宠物详情
     */
    @Column(notNull = true, length = 500, order = 12)
    private String petDetail;
    /**
     * 发布时间
     */
    @Column(notNull = true, order = 13)
    private String publishTime;
    /**
     * 点赞数
     */
    @Column(notNull = true, order = 14)
    private Integer likes;
    /**
     * 浏览数
     */
    @Column(notNull = true, order = 15)
    private Integer views;
    /**
     * 评论数
     */
    private Integer reviews;

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

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetSex() {
        return petSex;
    }

    public void setPetSex(String petSex) {
        this.petSex = petSex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
        this.petColor = petColor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPetDetail() {
        return petDetail;
    }

    public void setPetDetail(String petDetail) {
        this.petDetail = petDetail;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
