package hpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;




public class HPL extends JFrame  implements HyperlinkListener{

    private LoadResourcesAsStreamFromClass resource = new LoadResourcesAsStreamFromClass("texts/");
    private Properties props = new Properties();
    private JTextField searchTextField;
    private JMenu fileMenu, helpMenu;
    private JLabel storyLabel = new JLabel("Select Story");
    private Color beige = new Color(231,232,208);
    private Color darkGray = new Color(128,128,128);
    ModJEditorPane displayEditorPane;
 
  public HPL(){

    InputStream is = HPL.class.getClassLoader().getResourceAsStream("hpl.properties");
    try {
        props.load(is);
    } catch (IOException e1) {
        e1.printStackTrace();
    }
    this.setTitle(props.getProperty("app.title"));
    
    /* Use an appropriate Look and Feel */
    try {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
        //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        
        // select the Look and Feel
       // UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        
        com.jtattoo.plaf.hifi.HiFiLookAndFeel.setTheme("Default", "INSERT YOUR LICENSE KEY HERE", "my company");
        
        // select the Look and Feel
        UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        
        
    } catch (UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
    } catch (IllegalAccessException ex) {
        ex.printStackTrace();
    } catch (InstantiationException ex) {
        ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
    }
    /* Turn off metal's use of bold fonts */
    //UIManager.put("swing.boldMetal", Boolean.FALSE);
    
    this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/elder-sign-2.png")));

    
    setSize(1000, 700);

    // Handle closing events.
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        actionExit();
      }
    });

    //CENTER ON SCREEN
    Toolkit toolkit = getToolkit();
    Dimension size = toolkit.getScreenSize();
    setLocation(size.width/2 - getWidth()/2, 
    size.height/2 - getHeight()/2);
    
//    UIManager.put( "Menu.selectionBackground" , darkGray );
//    UIManager.put( "MenuItem.selectionBackground" , darkGray );
//    UIManager.put( "ComboBox.selectionBackground" , darkGray );
//    UIManager.put( "TextField.selectionBackground" , darkGray );
    
    // Set up file menu.
    JMenuBar menuBar = new JMenuBar();
//    menuBar.setBackground(Color.black);
//    menuBar.setForeground(Color.white);
    menuBar.setBorder(new EmptyBorder(0, 0, 0, 0));   
    fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
//    fileMenu.setBackground(Color.black);
//    fileMenu.setForeground(Color.white);
    JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
    fileExitMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionExit();
      }
    });
//    fileExitMenuItem.setBackground(Color.black);
//    fileExitMenuItem.setForeground(Color.white);
    fileMenu.add(fileExitMenuItem);
    
    
    // HELP MENU //////////////////////////////////////////////////////////////////
    helpMenu = new JMenu("Help");
    helpMenu.setMnemonic(KeyEvent.VK_H);
//    helpMenu.setBackground(Color.black);
//    helpMenu.setForeground(Color.white);
    JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
    aboutMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          URL imgURL = this.getClass().getResource("images/hpl.jpg");
          SplashBitmap splash = new SplashBitmap(imgURL, props.getProperty("about.text"));
          splash.hideOnClick();
          splash.showSplash();
      }
    });
//    aboutMenuItem.setBackground(Color.black);
//    aboutMenuItem.setForeground(Color.white);
    helpMenu.add(aboutMenuItem); 
    
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);

    // Set up button panel.
    JPanel toolbar = new JPanel(new ModifiedFlowLayout((int) LEFT_ALIGNMENT));
    toolbar.setBorder(new EmptyBorder(0, 0, 4, 0));    
//    toolbar.setBackground(Color.black);
//    toolbar.setForeground(Color.white);
    
    final JComboBox fileCombo = new JComboBox();
    fileCombo.addItem("");
    fileCombo.setSelectedIndex(0);

    final HashMap<String, String> myFileList = resource.getFileMap();  
    // Sort the Map by key by putting it into a TreeMap
    Map<String, String> sortedMap = new TreeMap<String, String>(myFileList);
    Iterator<String> it = sortedMap.keySet().iterator(); 
    while(it.hasNext()) { 
        String key = it.next();
        fileCombo.addItem(key);
    }     
    fileCombo.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JComboBox jcmbType = (JComboBox) e.getSource();
            String story = (String) jcmbType.getSelectedItem();
            if(story.length() > 1) {
                readFile(myFileList.get(story));
            }
        }
    });
    
//    storyLabel.setBackground(Color.black);
    storyLabel.setForeground(Color.white);
//    fileCombo.setBackground(Color.black);
//    fileCombo.setForeground(Color.white);
    toolbar.add(storyLabel);
    toolbar.add(fileCombo);
    
    searchTextField = new JTextField(15);
    searchTextField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {   
          fileCombo.setSelectedIndex(0);
          actionSearch();          
        }
      }
    });
//    searchTextField.setBackground(Color.black);
//    searchTextField.setForeground(Color.white);
    toolbar.add(searchTextField);
    
    JButton searchButton = new JButton("Search");
    searchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fileCombo.setSelectedIndex(0);
        actionSearch();
      }
    });
    searchButton.setBackground(darkGray);
    toolbar.add(searchButton);

    // Set up page display.

   

    try {
        displayEditorPane = new ModJEditorPane();
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    displayEditorPane.setContentType("text/html");
    displayEditorPane.setEditable(false);
    displayEditorPane.addHyperlinkListener(this);
    displayEditorPane.setAutoscrolls(false);
    displayEditorPane.setBackground(beige);
    
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(toolbar, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(displayEditorPane),
    BorderLayout.CENTER);
  }

  // Exit this program.
  private void actionExit() {
    System.exit(0);
  }

  // Load and show the page specified in the location text field.
  private void actionSearch() {
      if(searchTextField.getText().trim().length() >0){
          displayEditorPane.setText("Searching . . .");
            //    URL verifiedUrl = verifyUrl(locationTextField.getText());
            //    if (verifiedUrl != null) {
            //      showPage(verifiedUrl, true);
            //    } else {
            //      showError("Invalid URL");
            //    }
          ArrayList<String> searchResults  = resource.searchFiles(searchTextField.getText());
          String page = "<html><body><h2>" + searchResults.size() + " Results Found for : <b color=red>" + searchTextField.getText() + "</b></h2>";
          String endPage ="</body></html>";      
          for(String result : searchResults) {
            page += result + "<p>";
          }      
          displayEditorPane.setText(page + endPage);
          displayEditorPane.setCaretPosition( 0 );
      }
  }

  // Show dialog box with error message.
  private void showError(String errorMessage) {
    JOptionPane.showMessageDialog(this, errorMessage,
      "Error", JOptionPane.ERROR_MESSAGE);
  }

  // Verify URL format.
  private URL verifyUrl(String url) {
    // Only allow HTTP URLs.
    if (!url.toLowerCase().startsWith("http://"))
      return null;
    // Verify format of URL.
    URL verifiedUrl = null;
    try {
      verifiedUrl = new URL(url);
    } catch (Exception e) {
      return null;
    }
    return verifiedUrl;
  }

  /* Show the specified page and add it to
     the page list if specified. */
  private void showPage(URL pageUrl, boolean addToList){
    // Show hour glass cursor while crawling is under way.
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    try {
      // Get URL of page currently being displayed.
      URL currentUrl = displayEditorPane.getPage();
      // Load and display specified page.
      displayEditorPane.setPage(pageUrl);
      // Get URL of new page being displayed.
      URL newUrl = displayEditorPane.getPage();    
      // Update location text field with URL of current page.
      searchTextField.setText(newUrl.toString());
    }catch (Exception e){
      // Show error messsage.
      showError("Unable to load page. " + e.getMessage());
    }finally{
      // Return to default cursor.
      setCursor(Cursor.getDefaultCursor());
    }
  }


  // Handle hyperlink's being clicked.
  public void hyperlinkUpdate(HyperlinkEvent event) {
    HyperlinkEvent.EventType eventType = event.getEventType();
    if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
      if (event instanceof HTMLFrameHyperlinkEvent) {
        HTMLFrameHyperlinkEvent linkEvent =
          (HTMLFrameHyperlinkEvent) event;
        HTMLDocument document =
          (HTMLDocument) displayEditorPane.getDocument();
        document.processHTMLFrameHyperlinkEvent(linkEvent);
      } else {
          showPage(event.getURL(), true);
      }
    }
  }

  public void readFile(String filename) {
      displayEditorPane.setEditorKit(new HTMLEditorKit());
      InputStreamReader reader;
      reader = new InputStreamReader(resource.getInputStream(filename));    
       try {
           displayEditorPane.read(reader, filename);
       } catch (IOException e) {
           e.printStackTrace();
       }
  }
  
  // Run the Mini Browser.
  public static void main(String[] args) {
    HPL browser = new HPL();
    browser.setVisible(true);
  }
}
