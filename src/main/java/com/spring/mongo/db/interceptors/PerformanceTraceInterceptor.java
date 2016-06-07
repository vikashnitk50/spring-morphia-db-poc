package com.spring.mongo.db.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.time.StopWatch;

public class PerformanceTraceInterceptor implements MethodInterceptor {

  public Object invoke(MethodInvocation invocation) throws Throwable {

    PerformanceTrace performanceTraceAnnotation = invocation.getMethod().getAnnotation(PerformanceTrace.class);

    if (performanceTraceAnnotation != null) {

      StopWatch stop = new StopWatch();

      stop.start();

      Object object = invocation.proceed();

      stop.stop();

      System.out.println("The method[" + invocation.getMethod() + "] " + "has been completed in milliseconds :"
          + stop.getTime());

      return object;

    } else {
      
      return invocation.proceed();
      
    }
  }

}
