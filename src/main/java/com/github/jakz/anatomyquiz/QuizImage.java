package com.github.jakz.anatomyquiz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
  BufferedImage image;
  List<Zone> areas;
  
  PointMapper mapper;
  ZoneCreator creator;
  
  Point2D.Float mousePosition;
  
  
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
  
  @Override
  public void paintComponent(Graphics og)
  {
    Graphics2D g = (Graphics2D)og;
          
    g.drawImage(image, mapper.x, mapper.y, mapper.x + mapper.bw, mapper.y + mapper.bh, 0, 0, image.getWidth(), image.getHeight(), null);
    
    g.setStroke(new BasicStroke(3.0f));
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    for (Zone poi : areas)
    {
      g.setColor(Color.RED);
      g.drawRect((int)(mapper.x + poi.area.x * mapper.bw), (int)(mapper.y + poi.area.y * mapper.bh), (int)( poi.area.width * mapper.bw), (int)( poi.area.height * mapper.bh));
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
    if (e.getButton() == MouseEvent.BUTTON3)
      creator.cancel();
    else
    {
      Point2D.Float pt = mapper.map(e.getX(), e.getY());
            
      if (pt != null)
        creator.add(pt);
    }
   
    repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mousePosition = mapper.map(e.getX(), e.getY());
    repaint();
  }
}