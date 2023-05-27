package com.test.pub.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (XylOrder)实体类
 *
 * @author LGCSpadger
 * @since 2021-07-17 20:17:14
 */
@Data
public class XylOrder implements Serializable {

    private static final long serialVersionUID = -83480708638763056L;
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 状态 0正常，1冻结
     */
    private Object sysStatus;
    /**
     * 最后更新人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 商品编码
     */
    private String goodsId;
    /**
     * 商品位置
     */
    private String goodsLocation;
    /**
     * 商品类型：房子|车位|储藏室|装修包 字典值
     */
    private Object goodsType;
    /**
     * 认购日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    /**
     * 当前维护人
     */
    private String currentMaintainer;
    /**
     * 订购人
     */
    private String maintainer;
    /**
     * 订购人名称
     */
    private String maintainerName;
    /**
     * 项目编码
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 认购定金金额
     */
    private Double bargain;
    /**
     * 客户手机号
     */
    private String phoneNumber;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 订单状态：订购|签约|回款|款清|交付， 字典值
     */
    private Object orderStatus;
    /**
     * 1对外结佣，2对内结佣
     */
    private Object commissionStatus;
    /**
     * 是否取消认购，1是，2不是
     */
    private Object cancel;
    /**
     * 取消认购人
     */
    private String cancelBy;
    /**
     * 取消认购时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
    /**
     * 删除标记
     */
    private Object deleted;
    /**
     * 上下架状态，1下架，2上架
     */
    private Object state;
    /**
     * 审批状态
     */
    private Object procinstStatus;
    /**
     * 款清时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;
    private String processinstanceId;

}