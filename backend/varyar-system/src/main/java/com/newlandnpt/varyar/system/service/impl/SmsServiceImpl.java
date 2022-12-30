package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.DataSource;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.core.text.Convert;
import com.newlandnpt.varyar.common.enums.DataSourceType;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.SysConfig;
import com.newlandnpt.varyar.system.mapper.SysConfigMapper;
import com.newlandnpt.varyar.system.service.ISmsService;
import com.newlandnpt.varyar.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 短信 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 发送短信
     */
    @Override
    public void sendSms(String msg){
        System.out.println("--sendSms--");
    }
}
