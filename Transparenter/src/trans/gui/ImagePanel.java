package trans.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by
 * Daniel Roesch
 * as "Nils Darmstrong".
 * -----------------
 * For "Transparenter",
 * on 02.05.2017, 23:29.
 */
public class ImagePanel extends JPanel {

  // TODO try image panel in transparenter gui

  private BufferedImage image;
  private int imageWidth;
  private int imageHeight;

  public ImagePanel() {

  }

  public ImagePanel(BufferedImage image) {
    this.image = image;
    this.imageWidth = image.getWidth();
    this.imageHeight = image.getHeight();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  public BufferedImage getImage() {
    return image;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
    this.imageWidth = image.getWidth();
    this.imageHeight = image.getHeight();
  }
}
