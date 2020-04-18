package com.cn.demo.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.demo.Dao.MobileCityReposity;
import com.cn.demo.domain.MobileCity;
import com.cn.demo.exceptions.RetryException;
import com.cn.demo.resp.LocationResp;
import com.cn.demo.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private ThreadPoolTaskExecutor executorService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    RetryService retryService;
    @Resource
    MobileCityReposity mobileCityReposity;

    public LocationResp getMobileLocation(final String  mobile){
          LocationResp locationResp=null;
          Cache cache= cacheManager.getCache("mobile");
          if(cache!=null){//内存中取
             Object object=cache.get(mobile);
             if(object!=null){
                 locationResp=(LocationResp)object;
                 return locationResp;
             }
          }
        MobileCity mobileCity=mobileCityReposity.findByMobile(mobile);
          if(mobileCity!=null){//从数据库获取
              locationResp.setCity(mobileCity.getCity());
              locationResp.setIsp(mobileCity.getIsp());
              locationResp.setProvice(mobileCity.getProvice());
              cache= cacheManager.getCache("mobile");//放入缓存
              cache.put(mobile,locationResp);
          }
         Future future= executorService.submit(new innerRunnable(mobile,locationResp));
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
            future.cancel(true);
            throw new RetryException(10002,"连接超时");
        }catch (ExecutionException e){
            e.printStackTrace();
            future.cancel(true);
            throw new RetryException(10001,"调用服务出错请稍后重试");
        }
        return locationResp;
    }

    class innerRunnable implements Runnable{

        private String mobile;

        private LocationResp locationResp;

        public innerRunnable(String mobile, LocationResp locationResp) {
            this.mobile = mobile;
            this.locationResp = locationResp;
        }

        @Override
        public void run() {
            locationResp=retryService.getResult(mobile);
        }
    }
}
