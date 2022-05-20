package com.ding.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ding.project.entity.Account;
import com.ding.project.entity.Role;
import com.ding.project.entity.RoleResource;
import com.ding.project.mapper.RoleMapper;
import com.ding.project.mapper.RoleResourceMapper;
import com.ding.project.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    /**
     * 新增角色及角色所具有的的资源
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(Role role) {
        save(role);
        Long roleId = role.getRoleId();
        List<Long> resourceIds = role.getResourceIds();
        if(CollectionUtils.isNotEmpty(resourceIds)){
            for(Long resourceId:resourceIds){
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }
        return true;
    }

    /**
     * 修改角色及角色所具有的的资源
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(Role role) {
        updateById(role);
        Long roleId = role.getRoleId();
        roleResourceMapper.delete(Wrappers.<RoleResource>lambdaQuery()
        .eq(RoleResource::getRoleId,roleId));

        List<Long> resourceIds = role.getResourceIds();
        if(CollectionUtils.isNotEmpty(resourceIds)){
            for(Long resourceId:resourceIds){
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }
        return true;
    }
}
