package answers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentImages {
    public ImageIcon icon;
    public BufferedImage img;

    //TODO: Hier die geholte URL übergeben und unten einfügen
    public  void createImageFromAttachedImageFile() {
        try {
            img = ImageIO.read(new URL(
                    //TODO: Hier den Link, den ich aus dem Bildkommentar geholt habe
                    "http://www.java2s.com/style/download.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: Das dann im AnswerDetailScreen auf ein Label setzen!
        icon = new ImageIcon(img);
    }

}