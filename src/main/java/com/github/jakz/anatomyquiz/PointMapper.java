package com.github.jakz.anatomyquiz;

import java.awt.geom.Point2D;

public class PointMapper
{
  public int x, y, bw, bh;
  
  public PointMapper()
  {
  }
  
  Point2D.Float map(int x, int y)
  {
    /* before image */
    if (x < this.x || y < this.y)
      return null;
    else if (x >= this.x + this.bw || y >= this.y + this.bh)
      return null;
    else
      return new Point2D.Float((x - this.x) / (float) this.bw, (y - this.y) / (float) this.bh);
  }
  
  void set(int x, int y, int bw, int bh)
  {
    this.x = x;
    this.y = y;
    this.bw = bw;
    this.bh = bh;
  }
}
