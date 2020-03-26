package com.github.jakz.anatomyquiz;

import java.awt.geom.Rectangle2D;

class Zone
{
  public final String name;
  public final Rectangle2D.Float bounds;
  
  public Zone(String name, float x, float y, float w, float h)
  {
    this.name = name;
    bounds = new Rectangle2D.Float(x, y, w, h);
  }
  
  public String toString()
  {
    return String.format("{%s, %2.2f, %2.2f, %2.2f, %2.2f}", name, bounds.x, bounds.y, bounds.width, bounds.height);
  }
}