package com.mode.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String imageName = "https://images2017.cnblogs.com/blog/1252893/201710/1252893-20171010145801699-1973329860.png";
        URL url;
        System.out.println(Thread.activeCount());
        try {
            url = new URL(imageName);
            InputStream inputStream = url.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream(
                    new File("C:\\upload\\image.png"));
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf, 0, buf.length)) != -1)
                fileOutputStream.write(buf, 0, length);
            inputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
