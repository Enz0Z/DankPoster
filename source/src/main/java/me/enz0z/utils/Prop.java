package me.enz0z.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
public class Prop {
 
    private static Properties prop = null;
    
    public Prop() {
        try (InputStream input = new FileInputStream("config.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Properties getAll() {
        return prop;
    }
    
    public static String getString(String key) {
        return prop.getProperty(key).toString();
    }
    
    public static Boolean getBool(String key) {
        return Boolean.parseBoolean(prop.getProperty(key));
    }
    
    public static Integer getInt(String key) {
        return Integer.parseInt(prop.getProperty(key));
    }
}