package com.ding.project.service;

import com.ding.project.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ding.project.vo.ResourceVO;
import com.ding.project.vo.TreeVO;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 根据角色id,查询该角色所具有的资源
     * @param roleId
     * @return
     */
    List<ResourceVO> listResourceByRoleId(Long roleId);

    /**
     * 查询系统资源，供前端组件渲染
     * @return
     */
    List<TreeVO> listResource(Long roleId,Integer flag);

    /**
     * 将资源转换为代码模块名称集合
     * @param resourceVOS
     * @return
     */
//    HashSet<String> convert(List<ResourceVO> resourceVOS);
}
