package org.me.ByBlueHeart.HDebugClient.Utils;

import java.awt.*;

public class RainbowUtils {
  private float hue = 0.0F;
  
  private float hue2 = 0.0F;
  
  public Color getColorValue(float saturation, float brightness, float value) {
    if (this.hue > 255.0F)
      this.hue = 0.0F; 
    Color color = Color.getHSBColor(this.hue2 / 255.0F, saturation, brightness);
    this.hue2 += value;
    return color;
  }
  
  public void reset() {
    this.hue = 0.0F;
  }
  
  public void addValue(float value) {
    this.hue += value;
    this.hue2 = this.hue;
  }
}
