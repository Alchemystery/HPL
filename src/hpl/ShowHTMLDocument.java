package hpl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.html.HTMLEditorKit;

public class ShowHTMLDocument extends JFrame{
   
    private JEditorPane editorPane = new JEditorPane();
    private LoadResourcesAsStreamFromClass resource = new LoadResourcesAsStreamFromClass("texts/");
     private HashMap<String, String> myFileList = resource.getFileMap();
    
   public ShowHTMLDocument(String title) {
       /* Use an appropriate Look and Feel */
       try {
           UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
           //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
       UIManager.put("swing.boldMetal", Boolean.FALSE);
       
       setSize(1000, 700);
       setTitle(title);
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       Toolkit toolkit = getToolkit();
       Dimension size = toolkit.getScreenSize();
       setLocation(size.width/2 - getWidth()/2, 
       size.height/2 - getHeight()/2);
              
       JScrollPane scrollPane = new JScrollPane(editorPane);

       JComboBox fileCombo = new JComboBox(); 
       fileCombo.setSelectedIndex(-1);
       fileCombo.setPreferredSize(new Dimension(140, 22));
       fileCombo.setMaximumSize(new Dimension(140, 22));

       // Sort the Map by key by putting it into a TreeMap
       Map<String, String> sortedMap = new TreeMap<String, String>(myFileList);
       Iterator<String> it = sortedMap.keySet().iterator(); 
       while(it.hasNext()) { 
           String key = it.next();
           //String val = myFileList.get(key); 
           fileCombo.addItem(key);
       } 
       

       
       fileCombo.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               JComboBox jcmbType = (JComboBox) e.getSource();
               String story = (String) jcmbType.getSelectedItem();
               readFile("./texts/" + myFileList.get(story));
           }
       });

       JSeparator separator = new JSeparator();
       separator.setForeground(Color.gray);
       
       final String COMBOPANEL = "Card with JComboBox";

       JPanel topPanel = new JPanel(new CardLayout());
       topPanel.add(fileCombo, COMBOPANEL);
       //topPanel.add(separator);
       
       Container cp = getContentPane();
       cp.setLayout(new BorderLayout());
       cp.add(topPanel, BorderLayout.NORTH);
       cp.add(scrollPane, BorderLayout.CENTER);       
       setVisible(true);
       
   }
   
  public static void main(String args[])throws Exception {   
      ShowHTMLDocument myHTML = new ShowHTMLDocument("H. P. Lovecraft Library");      
  }
  
  public void readFile(String filename) {
      editorPane.setEditorKit(new HTMLEditorKit());
      FileReader reader;
      try {
           reader = new FileReader(filename);    
           try {
               editorPane.read(reader, filename);
           } catch (IOException e) {
               e.printStackTrace();
           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
  }
  
}