package com.github.jakz.anatomyquiz;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.pixbits.lib.lang.Size;
import com.pixbits.lib.ui.UIUtils;

public class App 
{
  public static void main(String[] args)
  {
    QuizImage label = new QuizImage("lateral.jpg");
    
    var panel = UIUtils.buildFillPanel(label, new Size.Int(1024, 1024));
    var frame = UIUtils.buildFrame(panel, "AnatomyQuiz");
    
    frame.exitOnClose();
    frame.centerOnScreen();
    frame.setVisible(true);
  }
}
