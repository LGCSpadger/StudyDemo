//建立模块
//第一个参数是自定义的模块名，第二个参数是引用的模块名
var app = angular.module("myApp", []);

var username = "张三";

//创建控制器
app.controller("myController", function($scope){

    var username = "张三";

    $scope.list = [
        {name:'张三', math:99, chinese:88},
        {name:'李四', math:17, chinese:46},
        {name:'赵五', math:60, chinese:60}
    ];

    $scope.clickEvent = function () {
        alert("点击事件成功触发！！！")
    }

    $scope.mouseEnterEvent = function () {
        console.log("鼠标移入事件成功触发！！！")
        // $("#mouseEnterContent").attr("display","");
    }

    $scope.spSframe =function(){
        var inputId = ['input','input1','input2','input3','input4','input5'];//需要浮动显示内容的input id
        var floatDiv = document.getElementById('floatDiv');
        for(var k = 0; k < inputId.length; k++ ){
            var input = document.getElementById(inputId[k]);
            //显示浮动窗
            input.onmouseover = function (ev){
                var windowEvent = window.event; //避免windowEvent丢失
                setTimeout(function(){
                    floatDiv.innerText = ev.toElement.value || '(空)';
                    floatDiv.style.display = 'block';
                    var mousePosition = getMousePos(windowEvent); //获取鼠标位置
                    floatDiv.style.left = mousePosition.x + 'px';
                    floatDiv.style.top =  mousePosition.y + 'px';
                },200);
            };
            //隐藏浮动窗
            input.onmouseout = function (ev) {
                floatDiv.style.display = 'none';
            };

        }
    };

    //获取鼠标相当于文档位置
    function getMousePos(event) {
        var e = event || window.event;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        var x = e.pageX || e.clientX + scrollX;
        var y = e.pageY || e.clientY + scrollY;
        //alert('x: ' + x + '\ny: ' + y);
        return { 'x': x, 'y': y };
    }

});