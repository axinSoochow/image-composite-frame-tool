package com.ke.store.vr.camera.spi;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置，如果是服务提供方，则不启用
 *  </p>
 * 需要设置beanName，防止多个SpiAutoConfiguration生成一样的beanName从而冲突
 */
@Configuration(value = SpiAutoConfiguration.FEIGNCLIENT_NAME + ".spiAutoConfiguration")
@ConditionalOnExpression(SpiAutoConfiguration.EXCLUDE_SPI_PROVIDER)
@EnableFeignClients(basePackageClasses = {SpiAutoConfiguration.class})
public class SpiAutoConfiguration {

  /**
   * 服务标识，通常为spring.application.name，请更换为你的项目
   */
  public final static String FEIGNCLIENT_NAME = "store-vr-camera-server";
  static final String EXCLUDE_SPI_PROVIDER =
      "#{'${spring.application.name}'!='" + FEIGNCLIENT_NAME + "'}";
}
