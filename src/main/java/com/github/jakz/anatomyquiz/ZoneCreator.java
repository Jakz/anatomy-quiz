package com.github.jakz.anatomyquiz;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ZoneCreator
{
  public static enum Mode
  {
    POLYGON
  }
  
  private final List<Point2D.Float> points;
  private Mode mode;
  
  ZoneCreator()
  {
    points = new ArrayList<>();
    mode = Mode.POLYGON;
  }
  
  void reset()
  {
    points.clear();
  }
  
  void cancel()
  {
    reset();
  }
  
  void add(Point2D.Float pt)
  {
    points.add(pt);
  }
  
  Point2D.Float at(int index) { return points.get(index); }
  Mode mode() { return mode; }
  Point2D.Float last() { return points.get(points.size()-1); }
  boolean active() { return !points.isEmpty(); }
  int size() { return points.size(); }
}
