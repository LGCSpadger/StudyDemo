$(document).ready(function() {
    //toggle `popup` / `inline` mode
    $.fn.editable.defaults.mode = 'popup';     
    
    //make username editable
    $('#username').editable();
    
    //make status editable
    $('#status').editable({
        type: 'select',
        title: 'Select 状态',
        placement: 'right',
        value: 2,
        source: [
            {value: 1, text: '状态 1'},
            {value: 2, text: '状态 2'},
            {value: 3, text: '状态 3'}
        ]
        /*
        //uncomment these lines to send data on server
        ,pk: 1
        ,url: '/post'
        */
    });
});