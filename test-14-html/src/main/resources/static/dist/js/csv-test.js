// JavaScript Document
$(function(){
	$(".execute-btn").click(function () {
		var hideobj=document.getElementById("hidebg");
		hidebg.style.display="block"; //显示隐藏层
		hidebg.style.height=window.screen.height+"px"; //设置隐藏层的高度为当前页面高度
		document.getElementById("hidebox").style.display="block"; //显示弹出层
	});

	/*关闭弹窗*/
	$(".closeDialog,#cancelBtn").click(function(){
		$(".checkbox").prop('checked',false);
		$(".miaoShu")[0].value = "";
		$("#hidebg").hide()
	});

	/*复选框只能选中一个*/
	$('.database-checkbox').find('input[type=checkbox]').bind('click', function(){
		$('.database-checkbox').find('input[type=checkbox]').not(this).attr("checked", false);
	});

	$("#sureBtn").click(function () {
		var $checked = $(".checkbox:checked");
		console.log($checked,"$checked");
		console.log($checked.val(),"$checked.val()");
		console.log($checked.length,"$checked.length");
		if ($checked.length == 0) {
			alert("请选择数据库");
		} else {
			var $sql = $(".miaoShu").val();
			console.log($sql,"$sql");
			if ($sql == null || $sql.length == 0) {
				alert("请填写sql");
			} else {
				$.post("http://localhost:8080/csv/test",{database: $checked.val(), sql: $sql},function (rs) {
					console.log(rs,"rs");
				});
			}
		}
	});
});