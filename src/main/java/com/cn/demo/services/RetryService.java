package com.cn.demo.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.demo.Dao.MobileCityReposity;
import com.cn.demo.exceptions.RetryException;
import com.cn.demo.resp.LocationResp;
import com.cn.demo.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);
    @Autowired
    private CacheManager cacheManager;

    @Retryable(value = {RetryException.class},maxAttempts = 3,backoff = @Backoff(delay = 20000))
    public LocationResp getResult(String mobile){
        LocationResp locationResp=null;
        String url="https://tool.bitefu.net/shouji/"+"?mobile="+mobile;
        String json=null;
        try {
            json= HttpUtil.doGet(url);
            logger.info("mobile：{}调用外部服务返回结果:{}",mobile,json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(json!=null){
            if(Integer.valueOf(JSON.parseObject(json).get("status").toString())!=1){
                logger.warn("调用查询号码服务出错");
                throw new RetryException(10001,"调用服务出错请稍后重试");
            }
            locationResp  = JSONObject.parseObject(json,LocationResp.class);
            Cache cache= cacheManager.getCache("mobile");
            cache.put(mobile,locationResp);
        }
        return locationResp;
        }
}
