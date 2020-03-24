package com.github.jakz.anatomyquiz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

class QuizImage extends JLabel
{
  BufferedImage image;
  List<Zone> areas;
  
  PointMapper mapper;
  ZoneCreator creator;
  
  
  public QuizImage(String image)
  {
    try
    {
      this.image = ImageIO.read(new File(image));
      mapper = new PointMapper();
      computeMapping();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    areas = new ArrayList<>();
    areas.add(new Zone("test", 0.05f, 0.05f, 0.1f, 0.1f));
    
    creator = new ZoneCreator();
    
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        computeMapping();
      }
    });
  }
  
  void computeMapping()
  {
    final int dw = getWidth(), dh = getHeight();
    int fw, fh;
    
    final int iw = image.getWidth(), ih = image.getHeight();
    float ratio = iw / (float) ih;
    
    if (ratio > 1.0f)
    {
      fw = dw;
      fh = (int)((1 / ratio) * dh);
    }
    else
    {
      fw = (int)(ratio * dw);
      fh = dh;
    }
    
    final int bx = (dw - fw) / 2;
    final int by = (dh - fh) / 2;
    
    mapper.set(bx, by, fw, fh);
  }
  
  @Override
  public void paintComponent(Graphics og)
  {
    Graphics2D g = (Graphics2D)og;
          
    g.drawImage(image, mapper.x, mapper.y, mapper.x + mapper.bw, mapper.y + mapper.bh, 0, 0, image.getWidth(), image.getHeight(), null);
    
    g.setStroke(new BasicStroke(1.2f));
    
    for (Zone poi : areas)
    {
      g.setColor(Color.RED);
      g.drawRect((int)(mapper.x + poi.area.x * mapper.bw), (int)(mapper.y + poi.area.y * mapper.bh), (int)( poi.area.width * mapper.bw), (int)( poi.area.height * mapper.bh));
    }
  }
}