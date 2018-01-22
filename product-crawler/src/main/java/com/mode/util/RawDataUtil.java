package com.mode.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by zhaoweiwei on 2017/12/7.
 */
public class RawDataUtil {

    private static final String RESOURCE_FILE_PATH = "/src/main/resources/";

    /**
     * line processor
     *
     * @param filePath
     * @return
     */
    public static void processLine(final String filePath, LineProcessor lineProcessor) {
        InputStream is = null;
        BufferedReader bufferedReader = null;
        try {
            Resource resource = new ClassPathResource(filePath);
            is = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            while (bufferedReader.ready()) {
                lineProcessor.processLine(bufferedReader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
