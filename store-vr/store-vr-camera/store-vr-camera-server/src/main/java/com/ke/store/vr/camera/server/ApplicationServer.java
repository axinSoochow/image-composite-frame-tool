package com.ke.store.vr.camera.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 
 * 程序启动入口
 * 
 * @author Jail Hu
 * @Copyright (c) 2017, Lianjia Group All Rights Reserved.
 */
@SpringBootApplication
public class ApplicationServer {

  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ApplicationServer.class.getName());

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(ApplicationServer.class, args);
    String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
    for (String profile : activeProfiles) {
      log.info("当前环境为:"+profile);
    }
    log.info("spring store vr 启动成功...");
  }
}
