package hpl;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ModJEditorPane extends JEditorPane{

      private final BufferedImage bufferedImage;
      private final TexturePaint texturePaint;


      public ModJEditorPane() throws IOException{
        super();
        bufferedImage = ImageIO.read(this.getClass().getResource("images/parch.jpg"));
        Rectangle rect = new Rectangle(0, 0, bufferedImage.getWidth(null), bufferedImage.getHeight(null));
        texturePaint = new TexturePaint(bufferedImage, rect);
        setOpaque(false);
      }

      /**
       * Override the painComponent method to do our image drawing.
       */
      public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(texturePaint);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
      }

      public static void main(String[] args) throws Exception{
        final JFrame frame = new JFrame("Java TextArea with Background Image");
        final ModJEditorPane textArea = new ModJEditorPane();
        final JScrollPane scrollPane = new JScrollPane(textArea);

        SwingUtilities.invokeLater(new Runnable(){
          public void run(){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            scrollPane.setPreferredSize(new Dimension(360,225));
            frame.getContentPane().add(scrollPane);
            textArea.setText("Some sample text here ...");
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
          }
        });
        
      }
   
    
}
