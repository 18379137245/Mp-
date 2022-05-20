package com.ding.project.service.impl;

import com.ding.project.entity.Customer;
import com.ding.project.mapper.CustomerMapper;
import com.ding.project.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author ding
 * @since 2022-05-16
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
