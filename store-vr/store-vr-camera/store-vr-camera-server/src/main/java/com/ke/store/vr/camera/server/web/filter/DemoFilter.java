package com.ke.store.vr.camera.server.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 过滤器
 * 
 * @author huisman (liuhui01@lianjia.com)
 * @copyright (c) 2018, Lianjia Group All Rights Reserved.
 */
public class DemoFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {}

}
