package com.github.jakz.anatomyquiz;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.lang.Size;
import com.pixbits.lib.ui.UIUtils;

public class App 
{
  public static QuizTable table = new QuizTable("lateral.jpg");
  
  public static void main(String[] args)
  {
    try
    {
      if (!load())
        table = new QuizTable("lateral.jpg");
      
      QuizImage label = new QuizImage(table);
      
      var panel = UIUtils.buildFillPanel(label, new Size.Int(1024, 1024));
      var frame = UIUtils.buildFrame(panel, "AnatomyQuiz");

      frame.addKeyListener(label.new KeyListener());
      frame.onClose(() -> StreamException.uncheck(App::save));
      frame.exitOnClose();
      frame.centerOnScreen();
      frame.setVisible(true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }
  
  public static boolean load() throws IOException
  {
    if (Files.exists(Paths.get("lateral.json")))
    {
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      
      table = gson.fromJson(new String(Files.readAllBytes(Paths.get("lateral.json"))), QuizTable.class);
      return true;
    }
    
    return false;
  }
  
  public static void save() throws IOException
  {
    System.out.println("Saving..");
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    
    Gson gson = builder.create();
    String content = gson.toJson(table, QuizTable.class);
    Files.write(Paths.get("lateral.json"), content.getBytes());
    
    System.out.println(content);
  }
}
