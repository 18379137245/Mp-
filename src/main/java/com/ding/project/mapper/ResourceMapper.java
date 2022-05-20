package com.ding.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ding.project.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ding.project.vo.ResourceVO;
import com.ding.project.vo.TreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询当前登录人的资源
     * @param wrapper
     * @return
     */
    List<ResourceVO> listResource(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper);

    List<TreeVO>listResourceByRoleId(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper,
                                     @Param("roleId") Long roleId);
}
