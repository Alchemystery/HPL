package hpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertyLoader {

    private Properties properties = new Properties();
    private String propFilename;
    
    public PropertyLoader(String filename){
        try {
            File file = new File(filename);
            properties.load(new FileInputStream(filename));
            propFilename = filename;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getProperty(String key){
       return properties.getProperty(key);
    }
    
    public void storeProperty(String key, String value){
        try {
            properties.put(key, value);
            properties.store(new FileOutputStream(propFilename), null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
