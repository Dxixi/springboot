package com.chixing.service;

import com.chixing.entity.Customer;

public interface CustomerService {
    public Customer getById(int id);
    public Customer getByCustId(int id);
}
