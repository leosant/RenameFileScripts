package com.github.leosant.config.view;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.github.leosant.util.LogUtilView;

public class JTextAreaAppenderInterceptorLog extends AppenderBase<ILoggingEvent> {

  @Override
  protected void append(ILoggingEvent eventObject) {
    System.out.println("vindo do interceptor log: " + eventObject.getMessage());

    LogUtilView.logToUI(eventObject.getMessage()+"\n");
  }
}
