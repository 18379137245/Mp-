package com.ding.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.entity.Account;
import com.ding.project.entity.Customer;
import com.ding.project.entity.Role;
import com.ding.project.service.AccountService;
import com.ding.project.service.ResourceService;
import com.ding.project.service.RoleService;
import com.ding.project.util.ResultUtil;
import com.ding.project.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AccountService accountService;
    /**
     * 进入列表页
     * @return
     */
    @RequestMapping(value = "/toList",method = RequestMethod.GET)
    public String toList(){
        return "role/roleList";
    }

    /**
     * 进入新增页
     * @return
     */
    @RequestMapping(value = "/toAdd",method = RequestMethod.GET)
    public String toAdd() {
        return "role/roleAdd";
    }

    /**
     * 查询方法
     * @param roleName
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public R<Map<String,Object>> list(String roleName,Long page,Long limit){
        QueryWrapper<Role> queryWrapper = Wrappers.<Role>query()
                .like(StringUtils.isNotBlank(roleName),"role_name",roleName)
                .orderByDesc("role_id");
        Page<Role> resultPage = roleService.page(new Page<>(page, limit), queryWrapper);

        /**
         * 第二种写法
         */
//        Page<Customer> myPage = customerService.lambdaQuery().like(StringUtils.isNotBlank(realName), Customer::getRealName, realName)
//                .like(StringUtils.isNotBlank(phone), Customer::getPhone, phone)
//                .orderByDesc(Customer::getCustomerId).page(new Page<>(page, limit));


        return ResultUtil.buildPageR(resultPage);
    }

    /**
     * 进入详情页
     * @return
     */
    @RequestMapping(value = "/toDetail/{id}",method = RequestMethod.GET)
    public String toDetail(@PathVariable Long id, Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return "role/roleDetail";
    }

    /**
     * 新增操作
     * @param role
     * @return
     */
    @PostMapping
    @ResponseBody
    public  R<Object> add(@RequestBody Role role){

        return ResultUtil.buildR(roleService.saveRole(role));
    }

    /**
     * 进入修改页
     * @return
     */
    @RequestMapping(value = "/toUpdate/{id}",method = RequestMethod.GET)
    public String toUpdate(@PathVariable Long id, Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return "role/roleUpdate";
    }


    /**
     * 修改操作
     * @param role
     * @return
     */
    @PutMapping
    @ResponseBody
    public  R<Object> update(@RequestBody Role role ){
        return ResultUtil.buildR(roleService.updateRole(role));
    }


    /**
     * 删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public  R<Object> delete(@PathVariable Long id, HttpSession session){
        Integer count = accountService.lambdaQuery().eq(Account::getRoleId,id).count();
        if(count > 0){
            return R.failed("有账号正拥有该角色");
        }
        return ResultUtil.buildR(roleService.removeById(id));
    }

    /**
     * 查询系统资源，供前端组件渲染
     * @return
     */
    @GetMapping({"listResource","listResource/{roleId}","listResource/{roleId}/{flag}"})
    @ResponseBody
    public R<List<TreeVO>> listResource(@PathVariable(required = false) Long roleId,
                                        @PathVariable(required = false) Integer flag){

        return R.ok(resourceService.listResource(roleId,flag));
    }
}
