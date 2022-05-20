layui.laydate.render({
    elem: '#createTimeRange',
    range: true
});

var table = layui.table;
// 第一个实例
var tableIns = table.render({
    id: 'test',
    elem: '#accountList',
    url: '/account/list', //数据接口
    page: true, //开启分页
    // response:{ // 这个不配置，不会渲染
    //     statusName:'code',
    //     statusCode:200
    // },
    parseData: function (res){ //res 即为原始返回的数据
        return{
            "code": res.code, //解析接口状态
            "msg": res.msg, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.records //解析数据列表
        };
    },
    cols: [[ //表头
        {field: 'username',title: '用户名'},
        {field: 'realName',title: '真实姓名'},
        {field: 'roleName',title: '角色名称'},
        {field: 'sex',title: '性别'},
        {field: 'createTime',title: '创建时间'},
        {title: '操作',toolbar:'#barDemo'}
    ]]
});

/**
 * 按条件查询
 */
function query(){
    tableIns.reload({
        where: {
            realName: $("#realName").val(),
            email: $("#email").val(),
            createTimeRange: $("#createTimeRange").val()
        },
        page: {
            curr: 1
        }
    });
}

/**
 * 进入新增页
 */
function intoAdd(){

    openLayer('/account/toAdd','新增账号')

    let form = layui.form;
    form.render();

    mySubmit("addSubmit",'POST');
}

// function addIds(){
//     layui
// }

//工具条事件
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    // var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    let accountId = data.accountId;
    if(layEvent === 'detail'){ //查看
        openLayer('/account/toDetail/'+accountId,'账号详情')
        //do somehing
    } else if(layEvent === 'del'){ //删除
        layer.confirm('真的删除行么', function(index){
            // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

            layer.close(index);
            myDelete('/account/'+accountId);

            // layer.close(index);
            // //向服务端发送删除指令
        });
    } else if(layEvent === 'edit'){ //编辑
        openLayer('/account/toUpdate/'+accountId,'编辑账号')

        // layui.form.render();
        let form = layui.form;
        form.render();

        mySubmit("updateSubmit",'PUT');
    }
});

layui.form.verify({
    checkUsername: function(value, item){ //value：表单的值、item：表单的DOM对象
        let error = null;
        let url = '/account/'+value;

        let accountId = $("input[name='accountId']").val();
        if(typeof (accountId) != 'undefined'){
            url += '/'+accountId;
        }
        $.ajax({
            url: url,
            async: false,
            type: 'GET',
            success: function (res){
                if(res.code == 0){
                    if(res.data > 0){
                        error = "用户名已经存在";
                    }
                }else {
                    error = "用户名检测出错";
                }
            },error:function (){
                error = "用户名检测出错";
            }
        });
        if(error !=null){
            return error;
        }
    }
});