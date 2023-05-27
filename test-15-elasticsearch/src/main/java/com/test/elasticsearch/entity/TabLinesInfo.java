package com.test.elasticsearch.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModelProperty;

/**
 * 专线信息表(TabLinesInfo)实体类
 *
 * @author liu.gc
 * @since 2022-12-01 15:57:18
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TabLinesInfo implements Serializable {

    private static final long serialVersionUID = -14455050355801478L;

    @ApiModelProperty(value = "id", name = "id", dataType = "Long", example = "id")
    private Long id;

    @ApiModelProperty(value = "专线编号", name = "lineNo", dataType = "String", example = "专线编号")
    private String lineNo;

    @ApiModelProperty(value = "产品实例标识", name = "productInstanceId", dataType = "String", example = "产品实例标识")
    private String productInstanceId;

    @ApiModelProperty(value = "集团编号", name = "groupNo", dataType = "Long", example = "集团编号")
    private Long groupNo;

    @ApiModelProperty(value = "集团名称", name = "groupName", dataType = "String", example = "集团名称")
    private String groupName;

    @ApiModelProperty(value = "客户服务等级", name = "customServiceLevel", dataType = "String", example = "客户服务等级")
    private String customServiceLevel;

    @ApiModelProperty(value = "是否总头专线", name = "isZtLine", dataType = "String", example = "是否总头专线")
    private String isZtLine;

    @ApiModelProperty(value = "A端传输设备", name = "aTransDev", dataType = "String", example = "A端传输设备")
    private String aTransDev;

    @ApiModelProperty(value = "A端传输设备端口", name = "aTransDevPort", dataType = "String", example = "A端传输设备端口")
    private String aTransDevPort;

    @ApiModelProperty(value = "Z端传输设备", name = "zTransDev", dataType = "String", example = "Z端传输设备")
    private String zTransDev;

    @ApiModelProperty(value = "Z端传输设备端口", name = "zTransDevPort", dataType = "String", example = "Z端传输设备端口")
    private String zTransDevPort;

    @ApiModelProperty(value = "海缆名称", name = "submarineCableName", dataType = "String", example = "海缆名称")
    private String submarineCableName;

    @ApiModelProperty(value = "业务保障等级", name = "businessGuaranteeLevel", dataType = "String", example = "业务保障等级")
    private String businessGuaranteeLevel;

    @ApiModelProperty(value = "催办等级", name = "urgeLevel", dataType = "String", example = "催办等级")
    private String urgeLevel;

    @ApiModelProperty(value = "客户属地", name = "customRevert", dataType = "String", example = "客户属地")
    private String customRevert;

    @ApiModelProperty(value = "二级属地", name = "secondLevelRevert", dataType = "String", example = "二级属地")
    private String secondLevelRevert;

    @ApiModelProperty(value = "接入方式", name = "accessWay", dataType = "String", example = "接入方式")
    private String accessWay;

    @ApiModelProperty(value = "A端地址", name = "aPortAddress", dataType = "String", example = "A端地址")
    private String aPortAddress;

    @ApiModelProperty(value = "Z端地址", name = "zPortAddress", dataType = "String", example = "Z端地址")
    private String zPortAddress;

    @ApiModelProperty(value = "覆盖范围", name = "coverage", dataType = "String", example = "覆盖范围")
    private String coverage;

    @ApiModelProperty(value = "跨省", name = "transProvincial", dataType = "String", example = "跨省")
    private String transProvincial;

    @ApiModelProperty(value = "业务发起省", name = "businessStartProvince", dataType = "String", example = "业务发起省")
    private String businessStartProvince;

    @ApiModelProperty(value = "业务开通日期", name = "businessStartDate", dataType = "String", example = "业务开通日期")
    private String businessStartDate;

    @ApiModelProperty(value = "开通计费日期", name = "businessStartChargeDate", dataType = "String", example = "开通计费日期")
    private String businessStartChargeDate;

    @ApiModelProperty(value = "拉远光路", name = "lygl", dataType = "String", example = "拉远光路")
    private String lygl;

    @ApiModelProperty(value = "状态", name = "status", dataType = "String", example = "状态")
    private String status;

    @ApiModelProperty(value = "停复机状态", name = "stopAndRunStatus", dataType = "String", example = "停复机状态")
    private String stopAndRunStatus;

    @ApiModelProperty(value = "来自集团", name = "isFromGroup", dataType = "String", example = "来自集团")
    private String isFromGroup;

    @ApiModelProperty(value = "电路名称", name = "circuitName", dataType = "String", example = "电路名称")
    private String circuitName;

    @ApiModelProperty(value = "传输电路一干编号", name = "circuitNo", dataType = "String", example = "传输电路一干编号")
    private String circuitNo;

    @ApiModelProperty(value = "电路群号", name = "circuitGroupNo", dataType = "String", example = "电路群号")
    private String circuitGroupNo;

    @ApiModelProperty(value = "维护属地", name = "maintainRevert", dataType = "String", example = "维护属地")
    private String maintainRevert;

    @ApiModelProperty(value = "维护属性", name = "maintainAttribute", dataType = "String", example = "维护属性")
    private String maintainAttribute;

    @ApiModelProperty(value = "CRM带宽", name = "crmBandwidth", dataType = "String", example = "CRM带宽")
    private String crmBandwidth;

    @ApiModelProperty(value = "当年投诉统计", name = "currYearComplaintNo", dataType = "Long", example = "当年投诉统计")
    private Long currYearComplaintNo;

    @ApiModelProperty(value = "当年重保次数", name = "currYearReinNo", dataType = "Long", example = "当年重保次数")
    private Long currYearReinNo;

    @ApiModelProperty(value = "备注", name = "remark", dataType = "String", example = "备注")
    private String remark;

    @ApiModelProperty(value = "传输设备名称", name = "transDev", dataType = "String", example = "传输设备名称")
    private String transDev;

    @ApiModelProperty(value = "传输局端设备名称", name = "transJdDev", dataType = "String", example = "传输局端设备名称")
    private String transJdDev;

    @ApiModelProperty(value = "局端设备名称", name = "jdDev", dataType = "String", example = "局端设备名称")
    private String jdDev;

    @ApiModelProperty(value = "传输设备端口", name = "transDevPort", dataType = "String", example = "传输设备端口")
    private String transDevPort;

    @ApiModelProperty(value = "传输局端设备端口", name = "transJdDevPort", dataType = "String", example = "传输局端设备端口")
    private String transJdDevPort;

    @ApiModelProperty(value = "局端设备端口", name = "jdDevPort", dataType = "String", example = "局端设备端口")
    private String jdDevPort;

    @ApiModelProperty(value = "终端IP地址(公网/私网)", name = "termIp", dataType = "String", example = "终端IP地址(公网/私网)")
    private String termIp;

    @ApiModelProperty(value = "设备互联IP", name = "devRelationIp", dataType = "String", example = "设备互联IP")
    private String devRelationIp;

    @ApiModelProperty(value = "用户互联IP", name = "customRelationIp", dataType = "String", example = "用户互联IP")
    private String customRelationIp;

    @ApiModelProperty(value = "用户IP地址段", name = "customIpAddress", dataType = "String", example = "用户IP地址段")
    private String customIpAddress;

    @ApiModelProperty(value = "客户业务接入地址", name = "customBusinessAddress", dataType = "String", example = "客户业务接入地址")
    private String customBusinessAddress;

    @ApiModelProperty(value = "是否重要专线", name = "isImportantLine", dataType = "String", example = "是否重要专线")
    private String isImportantLine;

    @ApiModelProperty(value = "企业建站控制", name = "enterpriseStationControl", dataType = "String", example = "企业建站控制")
    private String enterpriseStationControl;

}

