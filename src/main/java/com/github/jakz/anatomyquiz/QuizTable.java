package com.github.jakz.anatomyquiz;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizTable
{
  public String imageName;
  public List<Zone> zones;
  
  public QuizTable(String fileName)
  {
    imageName = fileName;
    zones = new ArrayList<>();
  }
  
  Optional<Zone> zoneAt(Point2D.Float pt)
  {
    return zones.stream()
        .filter(z -> z.bounds.contains(pt))
        .findAny();      
  }
}
