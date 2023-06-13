//正常的匿名函数
var sum1 = function (a,b) {
    return a + b;
}
console.log("正常的匿名函数输出：",sum1(1,1))
//箭头函数（多个参数）
var sum2 = (a,b) => {
    return a + b;
}
console.log("箭头函数（多个参数）输出：",sum2(1,1))
//箭头函数（一个参数）-- 仅有一个参数的话可以省略掉括号
var sum3 = param => {
    return param + 1;
}
console.log("箭头函数（一个参数）输出：",sum3(1))
