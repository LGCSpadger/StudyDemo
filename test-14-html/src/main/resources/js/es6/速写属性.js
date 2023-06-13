var name = "张三";
var age = 18;
// 常规写法
var person = {
    name: name,
    age: age
};
console.log(person)
//es6 写法。当一个对象的属性名和属性值的变量名相同时，可以简写成如下格式
var person_es6 = {
    name,
    age
};
console.log(person_es6)
