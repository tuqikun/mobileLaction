package com.cn.demo.configuration;

import com.cn.demo.services.DemoService;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class Config {

    @Bean
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(5);
        //配置最大线程数
        executor.setMaxPoolSize(5);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager=new CaffeineCacheManager();
        Caffeine caffeine=Caffeine.newBuilder()
                .initialCapacity(50)//缓存初始话大小
                .maximumSize(500);//缓存最大数量
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(cacheLoader());
        return cacheManager;
    }

    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }
            // 达到配置文件中的refreshAfterWrite所指定的时候回处罚这个事件方法
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                return oldValue; //可以在这里处理重新加载策略，本例子，没有处理重新加载，只是返回旧值。
            }
        };
        return cacheLoader;
    }

}
