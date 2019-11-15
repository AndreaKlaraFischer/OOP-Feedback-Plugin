package answers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://stackoverflow.com/questions/1064977/setting-background-images-in-jframe
public class ImagePanel extends Container {
    //15.11. Das zu Testzwecken.
  private Image image;
  public ImagePanel(Image image) {
      this.image = image;
  }

  protected void paintComponent() {

  }



}