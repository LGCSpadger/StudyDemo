<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
#### 遍历 list 数据 ###
<#-- 遍历 list 数据 -->
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>最好的朋友</td>
    </tr>
    <#-- 判断 studentList 集合是否存在，如果存在返回true，否则返回false -->
    <#if studentList ??>
        <#list studentList as student>
            <tr>
                <td>${student_index + 1}</td>
                <td <#if student.name == '唐三'>style="background:red;"</#if>>${student.name}</td>
                <td>${student.age}</td>
                <td>${student.money}</td>
                <#-- 缺失变量默认值使用 ! ，这里 ${(student.bestFriend.name)!} 表示如果 student 或 bestFriend 或 name 为空，那么默认显示空字符串 -->
                <td>${(student.bestFriend.name)!}</td>
            </tr>
        </#list>
    </#if>
</table>

<br/>
<br/>

#### 遍历 map 数据 ###
<#-- 遍历 map 数据 -->
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list studentMap ? keys as k>
        <tr>
            <td>${k_index + 1}</td>
            <td>${studentMap[k].name}</td>
            <td>${studentMap[k].age}</td>
            <td>${studentMap[k].money}</td>
        </tr>
    </#list>
</table>

<br/>
<br/>

### 直接输出 map 中的信息（方式一） ###<br/>
<#-- 直接输出 map 中的信息（方式一） -->
输出student01的信息：<br/>
姓名：${studentMap['student01'].name}<br/>
年龄：${studentMap['student01'].age}<br/>
<br/>
输出student02的信息：<br/>
姓名：${studentMap['student02'].name}<br/>
年龄：${studentMap['student02'].age}<br/>

<br/>
<br/>

### 直接输出 map 中的信息（方式二） ###<br/>
<#-- 直接输出 map 中的信息（方式二） -->
输出student01的信息：<br/>
姓名：${studentMap.student01.name}<br/>
年龄：${studentMap.student01.age}<br/>
<br/>
输出student02的信息：<br/>
姓名：${studentMap.student02.name}<br/>
年龄：${studentMap.student02.age}<br/>

<br/>
<br/>

### 内建函数的使用，格式：变量 + ? + 函数名称 ###<br/>
1、某个集合的大小<br/>
集合 studentList 的大小为：${studentList?size}<br/>
<br/>
2、日期格式化<br/>
显示年月日：${today?date}<br/>
显示时分秒：${today?time}<br/>
显示日期 + 时间：${today?datetime}<br/>
自定义格式化：${today?string("yyyy年MM月dd日")}<br/>
<br/>
3、内建函数 c ，freemarker显示数值时，默认会每隔 3 位用逗号隔开，如果不想用逗号隔开，就需要使用函数 c <br/>
未使用函数 c 的样子：${point}<br/>
使用函数 c 后的样子：${point?c}<br/>
<br/>
4、将 json 字符串转成对象 <br/>
<#assign text="{'name': '唐三', 'bank': '工商银行', 'account': '122648965489448'}" />
<#assign data=text?eval />
客户 ${data.name} 的开户行：${data.bank}，账号：${data.account} <br/>
<#-- 赋值的方式 -->
<#assign text=jsonData />
<#assign data=text?eval />
学生 ${data.name} 的年龄：${data.age}，最好的朋友：${(data.bestFriend.name)!} <br/>
<br/>

</body>
</html>