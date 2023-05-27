/**
 * @author Hupengpeng
 * @date 2019/5/16
 */
;(function () {
    /*jshint eqeqeq:false curly:false latedef:false */
    "use strict";
    $.fn.pagerhelper = function (opt) {
        var defaults = {};
        var _opt = $.extend(defaults, opt);
        $(this).parent().attr('id', 'tableParent');
        var eid = $(this).selector;
        //console.log(_opt)
        var _this = $(this);
        _this.parent().css("margin-top", "10px");
        var p = new Pager({
            totalCount: _opt.pager.total,
            pageSize: _opt.pager.pageSize,
            buttonSize: Math.ceil(parseInt(_opt.pager.total || 0) / parseInt(_opt.pager.pageSize || 10)),
            pageParam: 'pageNumber',
            first: _opt.pager.first,
            end: _opt.pager.end,
            show: _opt.show,
            elemet: eid
        });
        _this.parent().after(p.constructor.Html());
        return p.constructor;
    }

})();
/**
 * js分页
 */
var Pager = function (obj) {
    var totalCount = parseInt(obj.totalCount || 0);	//总条数
    var pageSize = parseInt(obj.pageSize || 10);	//每页显示条数
    var buttonSize = parseInt(obj.buttonSize || 0);//显示的按钮数
    var pageParam = obj.pageParam || 'page';		//分页的参数
    var className = obj.className || 'pagination';	//分页的样式
    var prevButton = obj.prevButton || '&laquo;';	//向前翻按钮
    var nextButton = obj.nextButton || '&raquo;';	//向后翻按钮
    var firstButton = obj.firstButton || '';		//第一页按钮
    var lastButton = obj.lastButton || '';			//最后一页按钮
    var first = obj.first || '0';//显示第n到B
    var end = obj.end || '0';//显示第n到B
    var show = obj.show || false;//如果只有一页是否显示分页
    var _ajax = obj.ajax || true;
    var dataUrl = '';
    var elemet = obj.elemet || '';
    var param = null;
    var currentPage = 0;
    var totalPage = 0;

    Pager.getParam = function (name) {				//获取参数
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    Pager.replaceUrl = function (name, value) {		//替换url参数

        var oUrl = window.location.href;
        dataUrl = oUrl;
        var reg = new RegExp("(^|&)(" + name + "=)([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        var rurl;
        if (r != null) {
            rurl = oUrl.replace(eval('/' + r[0] + '/g'), r[1] + r[2] + value + r[4]);
        } else {
            rurl = oUrl + (oUrl.indexOf('?') > 0 ? '&' : '?') + name + '=' + value;
        }
        if (_ajax) return 'javascript:void(0)';
        return rurl;
    }
    Pager.bindClick = function (page, el) {
        if (!_ajax || dataUrl == '' || !page) return;

        if (page == 'prev') {
            if (currentPage == 1) {
                return;
            }
            $(".pagination li").removeClass('active');
            currentPage = currentPage - 1;
            page = currentPage;
            $(".current-p-" + page).addClass("active");
        } else if (page == 'next') {
            if (currentPage == totalPage) {
                return;
            }
            $(".pagination li").removeClass('active');
            currentPage = currentPage + 1;
            page = currentPage;
            $(".current-p-" + page).addClass("active");
        } else {
            $(".pagination li").removeClass('active');
            currentPage = page;
            $(el).parent().addClass("active");
        }
        var _tempUrl = dataUrl + '?' + pageParam + '=' + page;
        if (param) {
            $.each(param, function (name, value) {
                _tempUrl += '&' + name + '=' + value;
            })
        }
        $('#tableParent').load(_tempUrl + ' ' + elemet);


        var _end = page * pageSize;
        var info = '当前显示 ' + (page == 1 ? 1 : ((page - 1) * pageSize + 1)) + ' 至 ' + (_end < totalCount ? _end : totalCount) + ' 条，共 ' + totalCount + ' 条。';
        $('#mainTable_info').html(info);
        if (currentPage == totalPage) {
            $(".pagination .next").addClass('disabled');
        } else {
            $(".pagination .next").removeClass('disabled');
        }
        if (currentPage == 1) {
            $(".pagination .prev").addClass('disabled');
        } else {
            $(".pagination .prev").removeClass('disabled');
        }
    }
    Pager.search = function (_param) {
        param = _param
        Pager.bindClick(1, null, _param);
    }
    Pager.Html = function () {

        if ((totalCount == 0 || totalCount <= pageSize) && !show) return '';

        var page = parseInt(Pager.getParam(pageParam)) || 0;
        page = page > 1 ? page : 1;

        var str = '<ul class="' + className + '">';
        if (firstButton) {
            str += '<li class="prev"><a onclick="Pager.bindClick(1,this)" id="page_first_a" href="' + Pager.replaceUrl(pageParam, 1) + '">' + firstButton + '</a></li>';
        }
        currentPage = page;
        if (page <= 1) {
            str += '<li class="prev disabled"><a onclick="Pager.bindClick(\'prev\',this)" href="' + Pager.replaceUrl(pageParam, page - 1) + '">' + prevButton + '</a></li>';
        } else {
            str += '<li class="prev"><a onclick="Pager.bindClick(\'prev\',this)" href="' + Pager.replaceUrl(pageParam, page - 1) + '">' + prevButton + '</a></li>';
        }
        var max = Math.ceil(totalCount / pageSize);
        totalPage = max;
        var start = Math.floor((page - 2) / (buttonSize - 2)) * (buttonSize - 2);
        start = start + buttonSize > max ? max - buttonSize : start;
        start = start >= 0 ? start : 0;
        for (var i = start + 1; i <= start + buttonSize; i++) {
            str += '<li class="current-p-' + i + (i == page ? ' active"' : '"') + '><a onclick="Pager.bindClick(' + i + ',this)" href="' + Pager.replaceUrl(pageParam, i) + '">' + i + '</a></li>';
        }
        if (page >= max) {
            str += '<li class="next disabled"><span>' + nextButton + '</span></li>';
        } else {
            str += '<li class="next"><a onclick="Pager.bindClick(\'next\',this)" href="' + Pager.replaceUrl(pageParam, page + 1) + '">' + nextButton + '</a></li>';
        }
        if (lastButton) {
            str += '<li class="next"><a onclick="Pager.bindClick(' + max + ',this)" href="' + Pager.replaceUrl(pageParam, max) + '">' + lastButton + '</a></li>';
        }
        var pageHtml = '       <div class="row col-xs-12">\n' +
            '                            <div class="col-sm-5">\n' +
            '                                <div class="dataTables_info" id="mainTable_info" role="status" aria-live="polite">当前显示 ' + first + '\n' +
            '                                    至 ' + end + ' 条，共 ' + totalCount + ' 条。\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                            <div class="col-sm-7">\n' +
            '                                <div class="dataTables_paginate paging_simple_numbers" id="mainTable_paginate">\n' +
            str + '</ul>'
        '                                </div>\n' +
        '                            </div>\n' +
        '                        </div>';
        Pager.bindClick();
        if (totalCount==0){
            pageHtml="<div class=\"row col-xs-12\" style='padding-left: 30px;'>无数据</div>";
        }
        return pageHtml;
    }
}
