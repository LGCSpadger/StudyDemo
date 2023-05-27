new Vue({
    el: '#app',
    data: {
        multipleSelection: [],
        tableData: [],
        currentPage: 1,
        pageSize: 10,
        total: 21
    },
    methods:{
        //判断ip地址的合法性
        checkIP(value){
            var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            var reg = value.match(exp);
            if(reg==null)
            {
                alert("开始的IP地址不合法！");
                return false;
            }
        },
        //比较两个ip地址的前后，,如果大于，返回1，等于返回0，小于返回-1
        compareIP(ipBegin, ipEnd)
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
        },
        indexAdd(index) {
            const page = this.currentPage; // 当前页码
            const pagesize = this.pageSize; // 每页条数
            return index + 1 + (page - 1) * pagesize;
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.currentPage = 1;
            this.pageSize = val;
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;
            this.getTableData(this.currentPage,this.pageSize);
        },
        getTableData: function (currentPage,pageSize) {
            console.log("currentPage: ",currentPage,"，pageSize：",pageSize);
            var self = this;
            console.log("this: ",this)

            var params = new URLSearchParams();
            params.append("pageNum",currentPage);
            params.append("pageSize",pageSize);

            axios.post(
                'http://localhost:8001/test11',
                params
            ).then(
                function(result) {
                    console.log("result999999: ",result);
                    self.tableData = result.data.list;
                    console.log("表格数据: ",self.tableData);
                    self.total = result.data.total;
                    console.log("总数: ",self.total);
                },
                function(err) {
                    console.log(err);
                }
            );
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        exportData() {
            axios.get("http://localhost:8001/test04").then(
                function(response) {
                    console.log("result999999: ",response);
                    var fileName = "文章信息.xls";
                    var blob = new Blob([response.data.data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                    var objectUrl = URL.createObjectURL(blob);
                    var excel = document.createElement('a');
                    document.body.appendChild(excel);
                    excel.setAttribute('style', 'display:none');
                    excel.setAttribute('href', objectUrl);
                    excel.setAttribute('download', fileName);
                    excel.click();
                    URL.revokeObjectURL(objectUrl);
                },
                function(err) {
                    console.log(err);
                }
            );
        }
    },
    created: function () {
        this.getTableData(this.currentPage,this.pageSize);
    },
    mounted: function () {

    }
})