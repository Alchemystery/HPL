package hpl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadResourcesAsStreamFromClass {

    private String fileStreamPath;
    private InputStream isStream;
    
    public LoadResourcesAsStreamFromClass(String path){
        fileStreamPath = path;
    }
    
    public ArrayList<String> getFileList(){
        isStream = getClass().getResourceAsStream(fileStreamPath + "index.txt");
        ArrayList<String> fileNameList = new ArrayList<String>();
            try{
                DataInputStream in = new DataInputStream(isStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                    fileNameList.add(strLine);
                }
                in.close();
                }catch (Exception e){             
                  System.err.println("Error : parseTitle " + e.getMessage());
                }  
         return fileNameList;
    }
    
    public HashMap<String, String> getFileMap(){
        HashMap<String, String> fileMap = new HashMap<String, String>();
        ArrayList<String> fileNameList = getFileList();
        File tempFile;
            for (String name : fileNameList) {
                tempFile= new File(name);
                String path = tempFile.getPath();
                String title = parseTitle(path).replace("<h2>", "").replace("</h2>", "").replace("<i>", "").replace("</i>", "");
                fileMap.put(title.trim(), name.trim());                 
            }
        return fileMap;    
    }
    
    private String parseTitle(String filename) {
        isStream = getClass().getResourceAsStream(fileStreamPath + filename);
        String title = ""; 
            try{
                DataInputStream in = new DataInputStream(isStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                   if(strLine.indexOf("<h2>") > -1) {
                       title = strLine;
                   }
                }
                in.close();
                }catch (Exception e){             
                  System.err.println("Error : parseTitle " + e.getMessage());
                }  
         return title;
    }
    
    public ArrayList<String> searchFiles(String searchTerm) {
        ArrayList<String> resultList = new ArrayList<String>();
        ArrayList<String> listOfFiles = getFileList();
        for(String file : listOfFiles) {            
            try{             
                isStream = getClass().getResourceAsStream(fileStreamPath + file);
                DataInputStream in = new DataInputStream(isStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null)   {                    
                   if(strLine.matches("(?i).*" + searchTerm + ".*")) {
                       String title = parseTitle(file).replace("<h2>", "").replace("</h2>", "").replace("<i>", "").replace("</i>", "");
                       String highlight = strLine.replaceAll("(?i)" + searchTerm, "<b color=red>" + searchTerm + "</b>");
                       resultList.add("<h3>" + title + "</h3>" + highlight);
                   }
                }
                in.close();
                }catch (Exception e){             
                  System.err.println("Error: " + e.getMessage());
                }  
        }
        return resultList;
    }
    
    public InputStream getInputStream(String filename) {
        return getClass().getResourceAsStream(fileStreamPath + filename);
    }
    
  public static void main( String[] args ) throws IOException {
        
      LoadResourcesAsStreamFromClass stories = new LoadResourcesAsStreamFromClass("texts/");
//      HashMap<String,String> fileNameList = stories.getFileMap();
//      Map<String, String> sortedMap = new TreeMap<String, String>(fileNameList);
//      Iterator<String> it = sortedMap.keySet().iterator(); 
//      while(it.hasNext()) { 
//          String key = it.next();  
//          String val = sortedMap.get(key); 
//          System.out.println("key: " + key + " value: " + val);
//      }
//      
      
      ArrayList<String> fileList = stories.getFileList();
      for(String name : fileList) {
          System.out.println(name);
      }
  }
  
  
}