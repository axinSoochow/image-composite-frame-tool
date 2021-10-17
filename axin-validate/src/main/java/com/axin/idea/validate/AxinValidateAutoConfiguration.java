package com.axin.idea.validate;


import com.axin.idea.validate.aop.ValidateAop;
import com.axin.idea.validate.support.ValidateSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(AxinValidateCacheProperties.class)
public class AxinValidateAutoConfiguration {

    @Autowired
    private AxinValidateCacheProperties properties;

    @Bean("ValidateAop")
    @ConditionalOnBean(AbstractValidate.class)
    public ValidateAop beanAop(AbstractValidate abstractValidate) {
        log.info("===========================================");
        log.info("=                                         =");
        log.info("=           Axin Validate Start           =");
        log.info("=                                         =");
        log.info("===========================================");
        ValidateSupport.setCache(properties);
        return new ValidateAop(abstractValidate);
    }

}
