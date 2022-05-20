package com.ding.project.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.entity.Account;
import com.ding.project.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
public interface RoleService extends IService<Role> {
    /**
     * 新增角色及角色所具有的的资源
     * @param role
     * @return
     */
    boolean saveRole(Role role);

    /**
     * 修改角色及角色所具有的的资源
     * @param role
     * @return
     */
    boolean updateRole(Role role);
}
