package com.test.pub.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (WxzwZdyxqk)实体类
 *
 * @author LGCSpadger
 * @since 2021-05-17 21:39:29
 */
@Data
public class WxzwZdyxqk implements Serializable {

    private static final long serialVersionUID = -78793007881012036L;
    private Integer id;
    private String areaName;
    private Integer termNum;
    private Object offlineTime;
    private Object onlineTime;
    private String jlrq;
    private String wxzwZdzxl;
    private String yxZdzxl;

}