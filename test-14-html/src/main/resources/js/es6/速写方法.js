// 常规写法
var person = {
    name: "张三",
    age: 18,
    sayHello: function () {
        console.log("你好！")
    }
};
person.sayHello();
//es6 写法。使用es6对方法进行简写
var person_es6 = {
    name: "张三",
    age: 18,
    sayHello() {
        console.log("你好！es6！")
    }
};
person_es6.sayHello();
