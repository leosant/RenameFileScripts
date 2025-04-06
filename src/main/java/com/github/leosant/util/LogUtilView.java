package com.github.leosant.util;

import javax.swing.*;
import java.util.Objects;

public class LogUtilView {

  private static JTextArea logArea;

  public static void initLogArea(JTextArea logArea) {
    if (Objects.isNull(LogUtilView.logArea)) {
      LogUtilView.logArea = logArea;
    }
  }

  public static void logToUI(String message) {
    SwingUtilities.invokeLater(() -> {
      logArea.append(message + "\n");
      logArea.setCaretPosition(logArea.getDocument().getLength());
    });
  }

}
