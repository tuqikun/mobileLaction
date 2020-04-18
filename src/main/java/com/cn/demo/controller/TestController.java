package com.cn.demo.controller;

import com.cn.demo.exceptions.RetryException;
import com.cn.demo.resp.ApiResponse;
import com.cn.demo.resp.LocationResp;
import com.cn.demo.services.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    DemoService demoService;

    @RequestMapping(value = "test")
    public ApiResponse test(@RequestParam(value = "mobile")String mobile)throws Exception{

        LocationResp locationResp=null;
        try{
            locationResp = demoService.getMobileLocation(mobile);
        }catch (RetryException e){
            return new ApiResponse(e.getCode(),e.getMsg(),locationResp);
        }
        return new ApiResponse(locationResp);
    }

}
