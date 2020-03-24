package com.github.jakz.anatomyquiz;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ZoneCreator
{
  final List<Point2D> points;
  
  ZoneCreator()
  {
    points = new ArrayList<>();
  }
  
  void reset()
  {
    points.clear();
  }
  
  boolean active() { return !points.isEmpty(); }
}
