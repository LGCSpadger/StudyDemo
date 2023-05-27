<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
/**
 * 错单处理界面
 * @author czm(5243) tel:1234567890123
 * @version 1.0
 * @since 2009-8-24 02:14:31
 * 
 * <br>
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>错单管理</title>
<lk:res addRes="/Js/jQeuryCheckForm-linkage.js"/>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/tree.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/tab.css"/>" type="text/css">
<s:if test="polltime!=null && polltime!=0">
	<meta http-equiv="refresh" content="<s:property value="polltime"/>">
</s:if>
<script type="text/javascript">

$(function(){
	//初始化样式信息
	$("tr",$("#workTable")).each(function(){
		if (this.status == 0){
			this.className="trOutyellow";
			$(this).bind("mouseover",function(){
				this.className="trOveryellow";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutyellow";
			});
		}
		if (this.status == 1){
			this.className="trOutblue";
			$(this).bind("mouseover",function(){
				this.className="trOverblue";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutblue";
			});
		}
		if (this.status == 2){
			this.className="trOut";
			$(this).bind("mouseover",function(){
				this.className="trOver";
			});
			$(this).bind("mouseout",function(){
				this.className="trOut";
			});
		}
		if (this.status == 3){
			this.className="trOutred";
			$(this).bind("mouseover",function(){
				this.className="trOverred";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutred";
			});
		}
		if (this.status == 4){
			this.className="trOutred";
			$(this).bind("mouseover",function(){
				this.className="trOverred";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutred";
			});
		}
		if (this.status == 5){
			this.className="trOutblue";
			$(this).bind("mouseover",function(){
				this.className="trOverblue";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutblue";
			});
		}
		if (this.status == 6){
			this.className="trOutred";
			$(this).bind("mouseover",function(){
				this.className="trOverred";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutred";
			});
		}
		if (this.status == 7){
			this.className="trOutchense";
			$(this).bind("mouseover",function(){
				this.className="trOverchense";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutchense";
			});
		}
		if (this.status == 8){
			this.className="trOutyellow";
			$(this).bind("mouseover",function(){
				this.className="trOveryellow";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutyellow";
			});
		}
		if (this.status == 9){
			this.className="trOutshougong";
			$(this).bind("mouseover",function(){
				this.className="trOvershougong";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutshougong";
			});
		}
		if (this.status == 10){
			this.className="trOutshougong";
			$(this).bind("mouseover",function(){
				this.className="trOvershougong";
			});
			$(this).bind("mouseout",function(){
				this.className="trOutshougong";
			});
		}
	});
	
	//完成后关闭父页面的等待信息
	parent.idList.innerHTML = idList.innerHTML;
	parent.closeMsgDlg();
	parent.wsState();
});

</script>
</head>

<body>
<form name="frm" action="">
<div id="idList">
	<table class="listTable" width="98%" align="center" id="idTable" oncontextmenu="return false;">
		<thead>
			<tr>
				<th width="110" nowrap>序号</th>
				<th width="70" nowrap>日期</th>
				<th width="80" nowrap>属地</th>
				<th width="70" nowrap>设备名称</th>
				<th width="70" nowrap>设备IP</th>
				<th width="120" nowrap>设备厂家</th>
				<th width="140" nowrap>设备型号</th>
				<th width="140" nowrap>异常分数</th>
				<th width="140" nowrap>异常分数</th>
			</tr>
		</thead>
		<tbody id="workTable">
		<s:if test="workView.size!=0">
			<s:iterator value="workView">
				<tr status="<s:property value="worksheetStatus"/>" 
						parames="<s:property value="worksheet_id"/>;<s:property value="sheet_id"/>;<s:property value="product_id"/>;<s:property value="worksheet_receive_time"/>;<s:property value="workParam"/>" 
						ondblclick="showDetail('<s:property value="worksheet_id"/>','<s:property value="sheet_id"/>','<s:property value="product_id"/>','<s:property value="worksheet_receive_time"/>')"
						onclick="doClick(this)" oncontextmenu="showmenuie5()" value="<s:property value="worksheetStatus"/>">
					<td nowrap><s:property value="sheet_id"/></td>
					<td nowrap><s:property value="city_name"/></td>
					<td nowrap><s:property value="userName"/></td>
					<td nowrap><s:property value="serviceName"/></td>
					<td nowrap><s:property value="opername"/></td>
					<td nowrap><s:property value="statusName"/></td>
					<td nowrap><s:property value="worksheet_receive_time"/></td>
					<td><s:property value="worksheet_error_desc"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td nowrap colspan="10" align="center">没有工单记录</td>
			</tr>
		</s:else>
		</tbody>
	</table>
</div>
</form>
</body>
</html>