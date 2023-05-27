/**
 * 页面初始化加载的方法，页面加载完后会自动调用此方法
 * 注意：
 * 1、$(function() {}) 是 $(document).ready(function()的简写
 * 2、这个函数什么时候执行的呢？DOM加载完毕之后执行。
 * 3、DOM是什么？DOM就是一个html页面的标签树，树，树。
 * 4、那么什么时候，DOM加载完成了呢？即页面所有的 html 标签（包括图片等）都加载完了，即浏览器已经响应完了，加载完了，全部展现到浏览器界面上了。
 *
 * 特别注意：
 *      1、$(function () {}); 中的 $ 是 jQuery 的语法，源生的 js 中是没有 $ 符号的，如果要使用 $(function () {});，则一定要在 html 中引入 jquery，否则会报错：$ is not defined
 *      2、window.onload = function () {} 这个是纯 js 的写法（onload 不是 onLond，千万注意不能写成大写）
 */
// $(function () {
//     var date = new Date();
//     console.log("date：",date)
//     var targetTime = timeCalculation(date,4,2);
//     console.log("targetTime：",targetTime)
// });

/**
 * 页面初始化调用，DOM加载完毕之后执行（纯 js 写法）
 */
window.onload = function () {
    var date = new Date();
    console.log("date：",date)
    var targetTime = timeCalculation(date,4,2);
    console.log("targetTime：",targetTime)
}

/**
 * 注意：在html中绑定click事件需要加 分号，否则会报错：
 */
function zzctcEvent(){
    document.getElementById("bg").style.display='block'
}

/**
 * 将时间戳转化成24小时标准时间格式
 * @param timestamp 事件秒值（不是毫秒值）
 * @returns {string}
 */
function timestampToTime(timestamp){
    var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate()<10 ?'0'+(date.getDate())+' ':date.getDate()) + ' ';
    var h = date.getHours()<10 ?'0'+(date.getHours())+':':date.getHours() + ':';
    var m = date.getMinutes()<10 ?'0'+(date.getMinutes())+':':date.getMinutes()+ ':';
    var s = date.getSeconds()<10 ?'0'+(date.getSeconds()):date.getSeconds();
    return Y+M+D+h+m+s;
}

/**
 * 时间计算
 * @param primaryTime   原时间，格式：yyyy-MM-dd HH:mm:ss
 * @param flag  计算标记
 * @param dVal  差值
 */
function timeCalculation(primaryTime,flag,dVal) {
    let targetTime;
    switch (flag) {
        case 1://按秒加减
            primaryTime.setSeconds(primaryTime.getSeconds() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
        case 2://按分加减
            primaryTime.setMin(primaryTime.getMin() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
        case 3://按小时加减
            primaryTime.setHours(primaryTime.getHours() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
        case 4://按天加减
            primaryTime.setDate(primaryTime.getDate() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
        case 5://按月加减
            primaryTime.setMonth(primaryTime.getMonth() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
        case 6://按年加减
            primaryTime.setFullYear(primaryTime.getFullYear() + dVal);
            targetTime = dateFormat(primaryTime);
            break;
    }
    return targetTime;
}

/**
 * Date类型的时间格式化成标准的24小时格式 yyyy-MM-dd HH:mm:ss
 * @param date
 * @returns {string}
 */
function dateFormat(date){
    var year = (date.getFullYear()).toString(),
        month = (date.getMonth() + 1).toString();
    if(month <= 9){
        month = '0' + month;
    }
    var day = (date.getDate()).toString(),
        hour = (date.getHours()).toString(),
        min = (date.getMinutes()).toString(),
        minNum = date.getSeconds();
    if(day <= 9){
        day = '0' + day;
    }
    if(hour <= 9){
        hour = '0' + hour;
    }
    if(min <= 9){
        min = '0' + min;
    }
    var sec = (date.getSeconds()).toString();
    var secNum = date.getSeconds();
    if(secNum <= 9){
        sec = '0' + sec;
    }
    let newTime = year + '-' +
        month + '-' +
        day + ' ' +
        hour + ':' +
        min + ':' +
        sec;
    return newTime;
}

/**
 * 小数四舍五入保留两位小数
 * @param num
 * @returns {string|boolean}
 */
function getToDecimal(num) {
    var result = parseFloat(num);
    if (isNaN(result)) {
        return false;
    }
    result = Math.round(num * 100) / 100;
    var s_x = result.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2) {
        s_x += '0';
    }
    return s_x;
}

//判断ip地址的合法性
function checkIP(value){
    var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    var reg = value.match(exp);
    if(reg==null)
    {
        alert("开始的IP地址不合法！");
        return false;
    }
}

//比较两个ip地址的前后，,如果大于，返回1，等于返回0，小于返回-1
function compareIP(ipBegin, ipEnd)
{
    var temp1;
    var temp2;
    temp1 = ipBegin.split(".");
    temp2 = ipEnd.split(".");
    for (var i = 0; i < 4; i++)
    {
        if (temp1[i]>temp2[i])
        {
            return 1;
        }
        else if (temp1[i]<temp2[i])
        {
            return -1;
        }
    }
    return 0;
}