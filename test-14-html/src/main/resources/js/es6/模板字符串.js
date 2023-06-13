var person = {
    name: "张三",
    age: 18
};
//字符串拼接（包含换行）
var str = "my name is " + person.name + "\nmy age is " + person.age;
console.log(str)
console.log("-------------------------------------")
//字符串拼接（包含换行）-- es6简化（换行符可以用 \n ，也可以直接换行）
var str_es6_1 = `my name is ${person.name} \nmy age is ${person.age}`;
console.log(str_es6_1)
console.log("-------------------------------------")
//字符串拼接（包含换行）-- es6简化（换行符可以用 \n ，也可以直接换行）
var str_es6_2 = `my name is ${person.name} 
my age is ${person.age}`;
console.log(str_es6_2)
