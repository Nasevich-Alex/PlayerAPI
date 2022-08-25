package utils;

import aquality.selenium.browser.AqualityServices;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReaderDataProperties {
    protected static Properties properties;

    public static Properties loadProperties() {
        properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("src/main/resources/config.properties");
            properties.load(input);
        } catch (IOException e) {
            AqualityServices.getLogger().warn("File config.properties was not founded");
            throw new RuntimeException();
        } finally {

        }
        return properties;
    }
}