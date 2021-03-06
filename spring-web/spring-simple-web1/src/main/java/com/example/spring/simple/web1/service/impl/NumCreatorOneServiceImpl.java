package com.example.spring.simple.web1.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.spring.simple.web1.service.NumCreatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: one
 * @author: huss
 * @time: 2020/10/24 15:03
 */
@Slf4j
@Service
public class NumCreatorOneServiceImpl implements NumCreatorService<String> {

    @Override
    public String create() {
        log.info(">>>>>[NumCreatorOneServiceImpl]开始生成ID...");
        return UUID.randomUUID().toString();
    }
}
