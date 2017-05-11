package test;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by
 * Daniel Roesch
 * as "Nils Darmstrong".
 * -----------------
 * For "Transparenter",
 * on 03.05.2017, 21:14.
 */
public class Test {


  private void setRGBWithOldAlpha(BufferedImage image, int x, int y, Color c) {
    if (image.getAlphaRaster() != null) {
      int a = image.getAlphaRaster().getSample(x, y, 0);
      image.setRGB(x, y, c.getRGB());
      image.getAlphaRaster().setSample(x, y, 0, a);
    } else {
      image.setRGB(x, y, c.getRGB());
    }
  }

}
