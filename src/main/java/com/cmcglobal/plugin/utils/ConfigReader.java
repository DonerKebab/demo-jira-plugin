package com.cmcglobal.plugin.utils;



import java.io.*;

public class ConfigReader {

    protected BufferedReader readFileFromResourceFolder(String filePath) throws IOException {
        InputStream inputStream;
        String fileName = filePath;
        BufferedReader streamReader = null;
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream(fileName);

        if (inputStream != null) {
            streamReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
        }
        return streamReader;
    }


}
