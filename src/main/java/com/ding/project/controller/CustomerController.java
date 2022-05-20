package com.ding.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.entity.Customer;
import com.ding.project.service.CustomerService;
import com.ding.project.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author laoyuan
 * @since 2022-05-14
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 进入列表页
     * @return
     */
    @RequestMapping(value = "/toList",method = RequestMethod.GET)
    public String toList(){
        return "customer/customerList";
    }


    /**
     * 查询方法
     * @param realName
     * @param phone
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public R<Map<String,Object>> list( String realName,String phone,Long page,Long limit){
        LambdaQueryWrapper<Customer> wrapper = Wrappers.<Customer>lambdaQuery()
                .like(StringUtils.isNotBlank(realName),Customer::getRealName,realName)
                .like(StringUtils.isNotBlank(phone),Customer::getPhone,phone)
                .orderByDesc(Customer::getCustomerId);
        Page<Customer> myPage = customerService.page(new Page<>(page, limit), wrapper);

        /**
         * 第二种写法
         */
//        Page<Customer> myPage = customerService.lambdaQuery().like(StringUtils.isNotBlank(realName), Customer::getRealName, realName)
//                .like(StringUtils.isNotBlank(phone), Customer::getPhone, phone)
//                .orderByDesc(Customer::getCustomerId).page(new Page<>(page, limit));


        return ResultUtil.buildPageR(myPage);
    }

    /**
     * 进入新增页
     * @return
     */
    @RequestMapping(value = "/toAdd",method = RequestMethod.GET)
    public String toAdd(){
        return "customer/customerAdd";
    }


    /**
     * 新增客户
     * @param customer
     * @return
     */
    @PostMapping
    @ResponseBody
    public  R<Object> add(@RequestBody Customer customer ){
//        boolean success = customerService.save(customer);
        return ResultUtil.buildR(customerService.save(customer));
    }


    /**
     * 进入修改页
     * @return
     */
    @RequestMapping(value = "/toUpdate/{id}",method = RequestMethod.GET)
    public String toUpdate(@PathVariable Long id, Model model){
        Customer customer = customerService.getById(id);
        model.addAttribute("customer",customer);
        return "customer/customerUpdate";
    }


    /**
     * 修改客户信息
     * @param customer
     * @return
     */
    @PutMapping
    @ResponseBody
    public  R<Object> update(@RequestBody Customer customer ){
//        boolean success = customerService.save(customer);
        return ResultUtil.buildR(customerService.updateById(customer));
    }

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public  R<Object> delete(@PathVariable Long id){
//        boolean success = customerService.save(customer);
        return ResultUtil.buildR(customerService.removeById(id));
    }

    /**
     * 进入详情页
     * @return
     */
    @RequestMapping(value = "/toDetail/{id}",method = RequestMethod.GET)
    public String toDetail(@PathVariable Long id, Model model){
        Customer customer = customerService.getById(id);
        model.addAttribute("customer",customer);
        return "customer/customerDetail";
    }
}
