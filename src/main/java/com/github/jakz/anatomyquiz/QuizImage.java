package com.github.jakz.anatomyquiz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

class QuizImage extends JLabel implements MouseListener, MouseMotionListener
{
  enum Mode
  {
    ADD_ZONE,
    REMOVE_ZONE
  };
  
  Mode mode = Mode.ADD_ZONE;
  
  QuizTable table;
  
  BufferedImage image;
  
  PointMapper mapper;
  ZoneCreator creator;
  
  Point2D.Float mousePosition;
  
  
  public QuizImage(QuizTable table)
  {
    try
    {
      this.image = ImageIO.read(new File(table.imageName));
      mapper = new PointMapper();
      computeMapping();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    this.table = table;
    creator = new ZoneCreator();
    
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        computeMapping();
      }
    });
    
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
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
  
  private void line(Graphics2D g, float x1, float y1, float x2, float y2)
  {
    g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
  }
  
  private void rect(Graphics2D g, float x1, float y1, float x2, float y2)
  {
    g.drawRect((int)x1, (int)y1, (int)x2, (int)y2);
  }
  
  @Override
  public void paintComponent(Graphics og)
  {
    Graphics2D g = (Graphics2D)og;
          
    g.drawImage(image, mapper.x, mapper.y, mapper.x + mapper.bw, mapper.y + mapper.bh, 0, 0, image.getWidth(), image.getHeight(), null);
    
    g.setStroke(new BasicStroke(3.0f));
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    for (Zone poi : table.zones)
    {
      g.setColor(Color.RED);
      
      Point2D.Float origin = mapper.rasterize(poi.bounds.x, poi.bounds.y);
      Point2D.Float end = mapper.rasterize(poi.bounds.x + poi.bounds.width, poi.bounds.y + poi.bounds.height);
      
      rect(g, origin.x, origin.y, end.x - origin.x, end.y - origin.y);  
    }
    
    
    if (creator.active())
    {
      if (creator.mode() == ZoneCreator.Mode.POLYGON)
      {
        for (int i = 0; i < creator.size() - 1; ++i)
        {
          g.setColor(Color.GREEN);
          
          Point2D.Float from = mapper.rasterize(creator.at(i)); 
          Point2D.Float to = mapper.rasterize(creator.at(i+1));
          line(g, from.x, from.y, to.x, to.y);
        }
        
        if (mousePosition != null)
        {        
          Point2D.Float from = mapper.rasterize(creator.last()); 
          Point2D.Float to = mapper.rasterize(mousePosition);
          line(g, from.x, from.y, to.x, to.y);
        }
      }
    }
  }



  @Override public void mouseClicked(MouseEvent e) { }
  @Override public void mouseReleased(MouseEvent e) { }
  @Override public void mouseEntered(MouseEvent e) { }
  @Override public void mouseExited(MouseEvent e) { }
  @Override public void mouseDragged(MouseEvent e) { }
  
  @Override
  public void mousePressed(MouseEvent e)
  {
    final Point2D.Float pt = mapper.map(e.getX(), e.getY());

    switch (mode)
    {
      case ADD_ZONE:
      {
        if (e.getButton() == MouseEvent.BUTTON3)
          creator.cancel();
        else
        {                
          if (pt != null)
          {
            Zone zone = creator.add(pt);
          
            if (zone != null)
            {
              System.out.println(zone);
              
              table.zones.add(zone);
              creator.reset();
            }
          }
        }
        
        break;
      }
      case REMOVE_ZONE:
      {
        if (e.getButton() == MouseEvent.BUTTON1 && pt != null)
        {
          table.zoneAt(pt).ifPresent(zone -> table.zones.remove(zone));
        }
        
        break;
      }

    }


    repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mousePosition = mapper.map(e.getX(), e.getY());
    
    switch (mode)
    {
      case ADD_ZONE: break;
      
    }
    
    
    repaint();
  }
  
  public class KeyListener implements java.awt.event.KeyListener
  {
    @Override public void keyTyped(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e)
    {
      if (e.getKeyChar() == '1') mode = Mode.ADD_ZONE;
      else if (e.getKeyChar() == '2') mode = Mode.REMOVE_ZONE;
    }
  }
}