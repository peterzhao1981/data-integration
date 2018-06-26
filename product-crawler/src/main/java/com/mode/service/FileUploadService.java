package com.mode.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.userinfo.CurrentUserService;

@Service
public class FileUploadService {

    private String userName = CurrentUserService.getCurrentUser().getUserName();

    // 当文件太大时，会出现上传不完整的问题问题
    public String fileUpload(HttpServletRequest request) {
        String resultCode = "";
        BufferedOutputStream stream = null;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("multiFile");
        String rootPath = createUserFileDirecoty();// 当前用户创建的文件夹

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                stream = new BufferedOutputStream(
                        new FileOutputStream(new File(rootPath + file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                resultCode = "4";
            } catch (Exception e) {
                e.printStackTrace();
                resultCode = "3";// 上传过程出现异常
            }
        } else {
            resultCode = "9";// 说明file为空
        }

        return resultCode;
    }

    /*
     * 每一个用户使用本功能的时候都要新建一个文件夹
     */
    public String createUserFileDirecoty() {
        String filepathStr = ConfigInfo.rootUplaodPath + userName;
        File filePath = new File(filepathStr);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        return filepathStr + "\\";
    }

}
