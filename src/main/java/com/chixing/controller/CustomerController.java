package com.chixing.controller;

import com.chixing.common.JsonResult;
import com.chixing.entity.Customer;
import com.chixing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    public CustomerController(){
        System.out.println("CustomerController==============");
    }
    @RequestMapping("get/{id}")
    public JsonResult get(@PathVariable("id") int id){
        Customer customer = customerService.getById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("customer",customer);
        return JsonResult.createSuccessJsonResult(data);
    }

}
