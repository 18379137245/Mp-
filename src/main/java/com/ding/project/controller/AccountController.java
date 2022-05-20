package com.ding.project.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.entity.Account;
import com.ding.project.entity.Customer;
import com.ding.project.entity.Role;
import com.ding.project.query.AccountQuery;
import com.ding.project.service.AccountService;
import com.ding.project.service.RoleService;
import com.ding.project.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账号表 前端控制器
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;

    /**
     * 跳转到账号列表头
     * @return
     */
    @RequestMapping(value = "/toList",method = RequestMethod.GET)
    public String toList(){
        return "account/accountList";
    }


    /**
     * 查询账号列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public R<Map<String,Object>> list(AccountQuery query){

        QueryWrapper<Account> wrapper = Wrappers.<Account>query();
        wrapper.like(StringUtils.isNotBlank(query.getRealName()),"a.real_name",query.getRealName())
                .like(StringUtils.isNotBlank(query.getEmail()),"a.email",query.getEmail());
        String createTimeRange = query.getCreateTimeRange();
        if(StringUtils.isNotBlank(createTimeRange)){
            String[] timeArray = createTimeRange.split(" - ");
            wrapper.ge("a.create_time",timeArray[0])
                    .le("a.create_time",timeArray[1]);
        }
        wrapper.eq("a.deleted",0).orderByDesc("a.account_id");
        IPage<Account> myPage = accountService.accountPage(new Page<>(query.getPage(), query.getLimit()), wrapper);
        return ResultUtil.buildPageR(myPage);
    }

    /**
     * 进入新增页面
     * @return
     */
    @RequestMapping(value = "/toAdd",method = RequestMethod.GET)
    public String toAdd(Model model){
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        model.addAttribute("roles",roles);
        return "account/accountAdd";
    }


    /**
     * 新增操作
     * @param account
     * @return
     */
    @PostMapping
    @ResponseBody
    public  R<Object> add(@RequestBody Account account){

        setPasswordAndSalt(account);
        return ResultUtil.buildR(accountService.save(account));
    }

    /**
     * 设置加密密码和加密
     * @param account
     */
    private void setPasswordAndSalt(Account account){

        String password = account.getPassword();
        String salt = UUID.fastUUID().toString().replaceAll("-","");
        MD5 md5 = new MD5(salt.getBytes());
        String digestHex = md5.digestHex(password);
        account.setPassword(digestHex);
        account.setSalt(salt);
    }

    /**
     * 进入修改页面
     * @return
     */
    @RequestMapping(value = "/toUpdate/{id}",method = RequestMethod.GET)
    public String toUpdate(@PathVariable Long id, Model model){
        Account account = accountService.getById(id);
        model.addAttribute("account",account);

        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        model.addAttribute("roles",roles);
        return "account/accountUpdate";
    }



    /**
     * 修改操作
     * @param account
     * @return
     */
    @PutMapping
    @ResponseBody
    public  R<Object> update(@RequestBody Account account ){

        if(StringUtils.isNotBlank(account.getPassword())){
            setPasswordAndSalt(account);
        }else {
            account.setPassword(null);
        }

        return ResultUtil.buildR(accountService.updateById(account));
    }

    /**
     * 进入详情页
     * @return
     */
    @RequestMapping(value = "/toDetail/{id}",method = RequestMethod.GET)
    public String toDetail(@PathVariable Long id, Model model){
        Account account = accountService.getAccountById(id);
        model.addAttribute("account",account);

        return "account/accountDetail";
    }

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public  R<Object> delete(@PathVariable Long id, HttpSession session){
        Account account = (Account)session.getAttribute("account");

        if(account.getAccountId().equals(id)){
            return R.failed("不能删除自己哦");
        }
//        boolean success = customerService.save(customer);
        return ResultUtil.buildR(accountService.removeById(id));
    }

    /**
     * 重名验证
     * @param username
     * @param accountId
     * @return
     */
    @GetMapping({"/{username}","/{username}/{accountId}"})
    @ResponseBody
    public  R<Object> delete(@PathVariable String username,
                             @PathVariable(required = false) String accountId){
//        boolean success = customerService.save(customer);
        Integer count = accountService.lambdaQuery()
                .eq(Account::getUsername, username)
                .ne(accountId !=null,Account::getAccountId,accountId)
                .count();
        return R.ok(count);
    }
}
