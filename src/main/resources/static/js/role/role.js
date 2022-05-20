var table = layui.table;
// 第一个实例
var tableIns = table.render({
    id: 'test',
    elem: '#roleList',
    url: '/role/list', //数据接口
    page: true, //开启分页
    parseData: function (res){ //res 即为原始返回的数据
        return{
            "code": res.code, //解析接口状态
            "msg": res.msg, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.records //解析数据列表
        };
    },
    cols: [[ //表头
        {field: 'roleName',title: '角色名称'},
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
            roleName: $("#roleName").val(),
        },
        page: {
            curr: 1
        }
    });
}

//工具条事件
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    // var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    let roleId = data.roleId;
    if(layEvent === 'detail'){ //查看
        openLayer('/role/toDetail/'+roleId,'角色详情')

        showTree('/role/listResource/'+roleId+'/1','resource',false);

        //do somehing
    } else if(layEvent === 'del'){ //删除
        layer.confirm('真的删除行么', function(index){
            // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

            layer.close(index);
            myDelete('/role/'+roleId);

            // layer.close(index);
            // //向服务端发送删除指令
        });
    } else if(layEvent === 'edit'){ //编辑
        openLayer('/role/toUpdate/'+roleId,'编辑角色')

        showTree('/role/listResource/'+roleId+'/0','resource');

        mySubmit("updateSubmit",'PUT',addIds);
    }
});

function toAdd(){
    openLayer('/role/toAdd','新增角色');

    showTree('/role/listResource','resource');

    mySubmit("addSubmit",'POST',addIds);
}

/**
 * 通用的资源树方法
 * @param url
 * @param id
 */
function showTree(url,id,showCheckbox){

    if(typeof (showCheckbox) == 'undefined'){
        showCheckbox = true;
    }
    $.ajax({
        url:url,
        async: false,
        type: 'GET',
        success: function (res){
            console.log("asda"+res.data);
            if(res.code == 0){
                layui.tree.render({
                    elem:'#'+id,
                    data:res.data,
                    id:id,
                    showCheckbox:showCheckbox
                });
            }
        }
    });
}

var addIds = function(field){
    let checkedData = layui.tree.getChecked('resource');

    field.resourceIds = getIds(checkedData,[]);
}
function getIds(checkedData,arr){
    for (let i in checkedData) {
        arr.push(checkedData[i].id);
        arr = getIds(checkedData[i].children,arr);
    }
    return arr;
}