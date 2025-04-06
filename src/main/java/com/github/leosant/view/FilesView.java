package com.github.leosant.view;

import javax.swing.*;
import java.awt.*;

import static com.github.leosant.util.LogUtilView.initLogArea;

public class FilesView {
  private static JTextArea logArea;

  public static void openView() {
    logArea = new JTextArea();
    SwingUtilities.invokeLater(FilesView::createAndShowGUI);
    initLogArea(logArea);
  }

  private static void createAndShowGUI() {
    JFrame frame = new JFrame("Rename Script - Status");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 400);

    logArea.setEditable(false);
    logArea.setBackground(Color.BLACK);
    logArea.setForeground(new Color(0, 255, 0));
    logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane(logArea);

    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
