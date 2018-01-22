package com.mode.util;

/**
 * Created by zhaoweiwei on 2017/12/7.
 */
public interface LineProcessor {
    /**
     * This method will be called once for each line.
     *
     * @param line the line read from the input, without delimiter
     * @return true to continue processing, false to stop
     */
    void processLine(String line) throws Exception;
}