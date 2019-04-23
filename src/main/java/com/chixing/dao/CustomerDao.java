package com.chixing.dao;

import com.chixing.entity.Customer;
import com.chixing.entity.CustomerExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * CustomerDao继承基类
 */
@Repository
@Mapper
public interface CustomerDao extends MyBatisBaseDao<Customer, Integer, CustomerExample> {
}