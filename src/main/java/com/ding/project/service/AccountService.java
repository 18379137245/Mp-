package com.ding.project.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.dto.LoginDTO;
import com.ding.project.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 账号表 服务类
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
public interface AccountService extends IService<Account> {

    LoginDTO login(String username, String password);

    /**
     * 分页查询账号
     * @param page
     * @param wrapper
     * @return
     */
    IPage<Account> accountPage(Page<Account> page, Wrapper<Account> wrapper);

    /**
     * 根据accountId查询账号信息
     * @param id
     * @return
     */
    Account getAccountById(Long id);
}
