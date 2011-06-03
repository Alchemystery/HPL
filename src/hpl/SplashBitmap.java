package hpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 * Displays a splash screen containing one bitmap file and an optional progress bar.
 * The splash screen can be selected to be hidden after a certain amount of time, when
 * the user clicks it or by calling a method.<br>
 * The bitmap used is loaded from a file. Valid file formats are GIF, JPEG and PNG.<br>
 * The progress bar can be determinate or indeterminate. When determinate, it completes
 * when the set timeout has passed.<br>
 * Everything including showing, configuration, updating and hiding the splash screen 
 * can also be done manually.<br>
 * <br>
 * Example #1:<br>
 * <br><code>
 * // specify a bitmap file<br>
 * SplashBitmap splash = new SplashBitmap("c:\\path\\to\\file.jpg");<br>
 * <br>
 * // make it close when clicked<br>
 * splash.hideOnClick();<br>
 * <br>
 * // show the whole thing<br>
 * splash.showSplash();<br>
 * </code><br><br>
 * Example #2:<br>
 * <br><code>
 * // specify a bitmap file<br>
 * SplashBitmap splash = new SplashBitmap(new URL("http://some.host.and/file.png"));<br>
 * <br>
 * // close after 2 seconds<br>
 * splash.hideOnTimeout(2000);<br>
 * <br>
 * // interrupt the timeout and close when the splash screen is clicked<br>
 * splash.hideOnClick();<br>
 * <br>
 * // add a progress bar that completes within these 2 seconds<br>
 * splash.addAutoProgressBar();<br>
 * <br>
 * // display a string in the progress bar<br>
 * splash.setProgressBarString("loading...");<br>
 * <br>
 * // show the whole thing<br>
 * splash.showSplash();<br>
 * </code>
 * 
 * 
 * @author Kai Blankenhorn &lt;<a href="mailto:pub01@bitfolge.de">pub01@bitfolge.de</a>&gt;
 */
public class SplashBitmap extends JWindow {

    private URL bitmapURL = null;
    private JLabel bitmapLabel;
    private Timer timeoutTimer = null;
    private Timer progressTimer = null;
    protected JProgressBar bar = null;
    private int timeout;
    private String displayHTML;
   

    /**
     * Demo program.
     */
    public static void main(String[] args) {
        URL imgURL = SplashBitmap.class.getResource("images/hpl.jpg");
        SplashBitmap splash = new SplashBitmap(imgURL, "TEST TO DISPLAY");
        splash.hideOnClick();
        //splash.hideOnTimeout(10000);
        //splash.addAutoProgressBar();
        //splash.setProgressBarString("loading...");
        splash.showSplash();
    }

    /**
     * Constructs a new splash screen with the bitmap from the given URL. To display this splash screen,
     * use <code>showSplash()</code>.
     * 
     * @param filename the URL of the image file to display
     * @param string 
     */
    public SplashBitmap(URL filename, String html) {
        this.bitmapURL = filename;
        this.displayHTML = html;
        this.init();
    }

    /**
     * Constructs a new splash screen with the bitmap from the given file name. To display this splash screen,
     * use <code>showSplash()</code>.
     * 
     * @param filename the name of the image file to display
     */
    @SuppressWarnings("deprecation")
    public SplashBitmap(String filename) {
        File bitmapFile = new File(filename);

        try {
            this.bitmapURL = bitmapFile.toURL();
        } catch(MalformedURLException ignore) {
            
        }

        this.init();
    }

    /**
     * Initializes the splash screen by constructing a JWindow and adding a JLabel containing the bitmap to it.
     * Sets the window position to the center of the screen.
     */
    private void init() {
        JLabel aboutText = new JLabel(displayHTML);
        aboutText.setPreferredSize(new Dimension(1, 1));
        Font myFont = new Font("Sans-Serif", Font.BOLD, 10);
        aboutText.setFont(myFont);
        Border paddingBorder = BorderFactory.createEmptyBorder(5,5,5,5);

        aboutText.setBorder(BorderFactory.createCompoundBorder(null, paddingBorder));
        this.bitmapLabel = new JLabel();
        ImageIcon bitmapImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.bitmapURL));
        int height = bitmapImage.getIconHeight();
        int width = bitmapImage.getIconWidth();
        bitmapLabel.setIcon(bitmapImage);
        bitmapLabel.setSize(width, height);
        bitmapLabel.setLocation(0, 0);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        bitmapLabel.setBorder(border);
        JPanel pan = new JPanel();
        pan.setBorder(new LineBorder(Color.black, 1));
        pan.setLayout(new BorderLayout());
        pan.add(bitmapLabel, BorderLayout.NORTH);
        pan.add(aboutText, BorderLayout.CENTER);
        this.getContentPane().add(pan,"Center");                
        this.setSize(width, height + 250);
        // this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2 - 180);
        this.setLocationRelativeTo(null);
    }

    /**
     * Shows this splash screen. The window will be centered on the screen. Notice that all
     * timers will start when this method is called, not when they are set. The splash screen
     * will be centered on the screen.
     */
    public void showSplash() {
        if(timeoutTimer != null) {
            timeoutTimer.start();
        }

        if(progressTimer != null) {
            progressTimer.start();
        }

        this.setVisible(true);
    }

    /**
     * Hides this splash screen and stops all running timers.
     */
    public void hideSplash() {
        this.setVisible(false);

        if(timeoutTimer != null) {
            timeoutTimer.stop();
            timeoutTimer = null;
        }

        if(progressTimer != null) {
            progressTimer.stop();
            progressTimer = null;
        }
    }

    /**
     * Tells this splash screen to close after a certain time has passed.
     * A splash screen can also be set to be hidden on timeout <b>and<b> on
     * click at the same time, either of which occurs first.
     * 
     * @param timeout the time this splash screen is to be displayed in milliseconds
     */
    public void hideOnTimeout(int timeout) {
        this.timeout = timeout;
        timeoutTimer = new Timer(timeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hideSplash();
            }
        });
        timeoutTimer.setRepeats(false);
    }

    /**
     * Tells this splash screen to close when being clicked.
     * A splash screen can also be set to be hidden on timeout <b>and<b> on
     * click at the same time, either of which occurs first.
     */
    public void hideOnClick() {
        this.addMouseListener(new MouseAdapter() {

            /**
             * Hides the splash screen when the mouse is clicked. Used internally for on-click closing.
             * 
             * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
             */
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    hideSplash();

                }else {
                    hideSplash(); 
                }                 
            }
        });
    }



}