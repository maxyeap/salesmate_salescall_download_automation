package com.example.salesmate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class Download {
    public static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    public static void startDownload(List<HashMap> downloadDetails, String folder) {
        System.out.println("Start downloading the files now...");
        for (HashMap log : downloadDetails) {
            String fileName = log.get("date") + "-" + log.get("duration") + "-" + log.get("name")
                    + "-" + log.get("agent");
            String downloadUrl = (String) log.get("url");
            try {
                downloadUsingStream(downloadUrl, folder + "/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
