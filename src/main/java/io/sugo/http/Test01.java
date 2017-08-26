package io.sugo.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Test01 {
    private static Properties properties = new Properties();


    public static void main(String[] args) throws IOException {
        properties.load(new FileInputStream("config.properties"));
        String[] str1 = properties.getProperty("receipt.areas").split(",");
        String[] str2 = properties.getProperty("receipt.type").split(",");

        System.out.println(str1.length);
        System.out.println(str2.length);
    }
}
