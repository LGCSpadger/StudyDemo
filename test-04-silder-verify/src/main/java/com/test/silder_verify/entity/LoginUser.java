package com.test.silder_verify.entity;

import lombok.Data;

//@Data
public class LoginUser {

    //账号
    private String account;
    //密码
    private String password;
    //验证码随机字符串
    private String nonceStr;
    //验证值
    private String value;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LoginUser() {
    }
}
