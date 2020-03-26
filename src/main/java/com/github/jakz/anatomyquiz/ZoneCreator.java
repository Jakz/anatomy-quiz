package com.github.jakz.anatomyquiz;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ZoneCreator
{
  public static enum Mode
  {
    POLYGON,
    RECTANGLE_BY_CENTER,
    RECTANGLE_BY_CORNER,
  }
  
  private final List<Point2D.Float> points;
  private Mode mode;
  
  ZoneCreator()
  {
    points = new ArrayList<>();
    mode = Mode.RECTANGLE_BY_CORNER;
  }
  
  void reset()
  {
    points.clear();
  }
  
  void cancel()
  {
    reset();
  }
  
  Zone add(Point2D.Float pt)
  {
    points.add(pt);

    switch (mode)
    {
      case POLYGON:
      {
        break;
      }
      case RECTANGLE_BY_CENTER:
      {
        if (size() == 2)
        {
          float dx = Math.abs(pt.x - first().x);
          float dy = Math.abs(pt.y - first().y);       
          return new Zone("", first().x - dx, first().y - dy, dx*2, dy*2);
        }    
       
        break;
      }
      case RECTANGLE_BY_CORNER:
      {
        if (size() == 2)
        {   
          Point2D.Float first = first(), last = last();
          
          /* sort coordinates so that first one is always upper left corner */
          if (last.x < first.x)
          {
            float tmp = first.x;
            first.x = last.x;
            last.x = tmp;
          }
          
          if (last.y < first.y)
          {
            float tmp = first.y;
            first.y = last.y;
            last.y = tmp;
          }

          return new Zone("", first.x, first.y, last.x - first.x, last.y - first.y);
        }    
       
        break;
      }
    }
    
    return null;
    
  }
  
  Point2D.Float at(int index) { return points.get(index); }
  Mode mode() { return mode; }
  
  Point2D.Float first() { return points.get(0); }
  Point2D.Float last() { return points.get(points.size()-1); }
  boolean active() { return !points.isEmpty(); }
  int size() { return points.size(); }
}
