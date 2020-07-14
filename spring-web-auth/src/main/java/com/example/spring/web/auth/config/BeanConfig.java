package com.example.spring.web.auth.config;

import com.example.spring.common.SnowFlakeCreator;
import com.example.spring.service.core.config.SnowFlakeCreatorProperty;
import com.example.spring.service.core.support.NumberCreatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.spring.common.cache.ICacheService;
import com.example.spring.web.core.aop.LogAop;
import com.example.spring.web.core.cache.RedisCacheServiceImpl;

/**
 * @description: bean
 * @author: huss
 * @time: 2020/6/23 15:44
 */
@Configuration
public class BeanConfig {

    @Autowired
    private SnowFlakeCreatorProperty snowFlakeCreatorProperty;
    /**
     * 请求日志打印
     *
     * @return
     */
    @Bean
    public LogAop logAop() {
        return new LogAop();
    }



    /**
     * 内置远程缓存为redis缓存
     *
     * @return
     */
    @Bean
    public <R> ICacheService<String, R> redisCacheService() {
        return new RedisCacheServiceImpl();
    }

    /**
     * 号码制造器
     *
     * @return
     */
    @Bean
    public NumberCreatorFactory numberCreatorFactory() {
        return new NumberCreatorFactory(
            new SnowFlakeCreator(snowFlakeCreatorProperty.getDataCenterId(), snowFlakeCreatorProperty.getMachineId()));
    }

}
