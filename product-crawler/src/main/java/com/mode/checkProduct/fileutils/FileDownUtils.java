package com.mode.checkProduct.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownUtils {
    // Excel文件下载
    public static void downExcelFile(HttpServletRequest request, HttpServletResponse response,
            String path) throws UnsupportedEncodingException, IOException {
        String name = "";
        File filePath = new File(path);
        name = filePath.getName();
        System.out.println(name);
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        // 第一步：设置响应类型
        response.setContentType("appllication/force-download");
        // 第二步：读取文件
        FileInputStream in = new FileInputStream(path);
        // 设置响应头,对文件进行url编码
        name = URLEncoder.encode(name, "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        response.setContentLength(in.available());
        // 开始copy
        OutputStream outputStream = response.getOutputStream();
        byte[] bs = new byte[1024];
        int len = 0;
        while ((len = in.read(bs)) != -1) {
            outputStream.write(bs, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        in.close();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
