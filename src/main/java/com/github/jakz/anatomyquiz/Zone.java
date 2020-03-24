package com.github.jakz.anatomyquiz;

import java.awt.geom.Rectangle2D;

class Zone
{
  public final String name;
  public final Rectangle2D.Float area;
  
  public Zone(String name, float x, float y, float w, float h)
  {
    this.name = name;
    area = new Rectangle2D.Float(x, y, w, h);
  }
}