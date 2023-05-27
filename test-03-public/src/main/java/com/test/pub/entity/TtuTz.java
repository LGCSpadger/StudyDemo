package com.test.pub.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * TTU终端台账表(TtuTz)实体类
 *
 * @author LGCSpadger
 * @since 2021-06-07 12:30:26
 */
@Data
public class TtuTz implements Serializable {

    private static final long serialVersionUID = 270527273951096460L;
    /**
     * id
     */
    private Long id;
    /**
     * 地市名称
     */
    private String cityName;
    /**
     * 单位名称
     */
    private String areaName;
    /**
     * 项目性质
     */
    private String xmxzName;
    /**
     * 变电站名称
     */
    private String facName;
    /**
     * 线路名称
     */
    private String feederName;
    /**
     * 终端名称
     */
    private String termName;
    /**
     * psr_id
     */
    private String psrId;
    /**
     * 终端类型名称
     */
    private String termTypeName;
    /**
     * 二/三遥
     */
    private String termFlag;
    /**
     * 是否投运
     */
    private String runStatus;
    /**
     * 通信方式
     */
    private String txfsName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 线路DA模式
     */
    private String daModel;
    /**
     * 终端ip
     */
    private String termIp;
    /**
     * 终端生产厂家
     */
    private String termSscj;
    /**
     * 在线时长
     */
    private String onTime;
    /**
     * 离线时长
     */
    private String offTime;
    /**
     * 终端在线率
     */
    private String zdzxl;

}