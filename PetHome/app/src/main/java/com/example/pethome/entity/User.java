package com.example.pethome.entity;

import com.database.lib.Column;

public class User {

    /**
     * 用户id
     */
    @Column(primaryKey = true, notNull = true, order = 1)
    private Integer id;
    /**
     * 用户名
     */
    @Column(notNull = true, length = 50, order = 2)
    private String username;
    /**
     * 密码
     */
    @Column(notNull = true, length = 50, order = 3)
    private String password;
    /**
     * 手机号
     */
    @Column(notNull = true, length = 11, order = 4)
    private String phone;
    /**
     * 头像
     */
    @Column(order = 5)
    private String avatar;
    /**
     * 注册时间
     */
    @Column(notNull = true, order = 6)
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
