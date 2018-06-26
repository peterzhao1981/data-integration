package com.mode.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.endtofront.JSONData;
import com.mode.checkProduct.excelProcess.ExcelCheck;
import com.mode.checkProduct.fileutils.FileDownUtils;
import com.mode.checkProduct.fileutils.FileUtils;
import com.mode.checkProduct.threadcontrol.CheckThreadJVM;
import com.mode.checkProduct.userinfo.CurrentUserService;
import com.mode.checkProduct.userinfo.User;
import com.mode.entity.CheckProductStatus;
import com.mode.service.CheckProductStatusService;
import com.mode.service.FileUploadService;

/**
 * Created by zhaoweiwei on 2017/11/13.
 */
@RestController
@RequestMapping(value = "/checkProduct")
public class CheckProductMainApi {

    @Autowired
    private CheckProductStatusService checkProductStatusService;

    @Autowired
    private FileUploadService fileUploadService;

    // 1、每一次开始新的爬虫之前，需要清库，或者手动将表删掉。启动程序即可重新建表
    @RequestMapping(value = "/excelProcess", method = RequestMethod.POST)
    public String excelProcess() throws Exception {
        System.out.println("开始导入");
        checkProductStatusService.excelProcess();
        System.out.println("导入excel至数据库成功");
        return "1";// 导入成功
    }

    // 2、增量式爬取，如果记录不为error url,lack将重新进行爬取。可能要调用该url多次
    @RequestMapping(value = "/checkProductStatus", method = RequestMethod.GET)
    public String checkProductStatus() throws Exception {
        return checkProductStatusService.process();
    }

    // 3、导出数据库数据到excel中,按类型导出
    @RequestMapping(value = "/exportDBtoExcel/{kind}", method = { RequestMethod.GET,
            RequestMethod.POST })
    public String exportDBtoExcelLessInfo(@PathVariable int kind, HttpServletResponse response,
            HttpServletRequest request) {
        // String path = ConfigInfo.rootUplaodPath +
        // CurrentUserService.getCurrentUser().getUserName()
        // + "\\output\\";
        String downFilePath = null;
        String result = "";
        downFilePath = checkProductStatusService.exportDBtoExcel(kind);
        if (downFilePath != null) {
            try {
                // kind=1，只导出url无效的excel，kind=2将会导出缺货信息
                FileDownUtils.downExcelFile(request, response, downFilePath);
                result = "success";
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                System.out.println("导出数据库数据失败");
                result = "error";
            }
        } else {
            result = "error";
        }
        // FileUtils.clearDirectory(path);
        return result;
    }

    @RequestMapping(value = "/getMainPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getInfo(ModelAndView mv, HttpServletRequest request) throws Exception {
        mv.setViewName("/mainPage");
        mv.addObject("title", "欢迎使用商品验证服务系统");
        return mv;
    }

    // excel上传
    @RequestMapping(value = "/excelupload", method = RequestMethod.POST)
    @ResponseBody
    public String excelupload(HttpServletRequest request) throws Exception {
        String resultCode = "3";
        User user = CurrentUserService.getCurrentUser();
        String path = ConfigInfo.rootUplaodPath + user.getUserName();
        resultCode = fileUploadService.fileUpload(request);

        return resultCode;
    }

    @RequestMapping(value = "/excelUploadPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView excelUploadPage(ModelAndView mv, HttpServletRequest request)
            throws Exception {
        mv.setViewName("/excelUpload");
        mv.addObject("title", "进入excel上传页面");
        return mv;
    }

    // 清理数据库记录
    @RequestMapping(value = "/deleteUserRecord", method = RequestMethod.GET)
    public String deleteUserRecord(HttpServletRequest request) {
        User user = CurrentUserService.getCurrentUser();
        try {
            checkProductStatusService.deleteUserRecord(user.getUserName());
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/clearFiles", method = RequestMethod.GET)
    public String clearFiles(HttpServletRequest request) {
        User user = CurrentUserService.getCurrentUser();
        System.out.println("开始清理文件夹");
        String path = ConfigInfo.rootUplaodPath + user.getUserName();
        try {
            FileUtils.clearDirectory(path);
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    // 查询1688是否到达调用极限
    @RequestMapping(value = "/get1688APIUseCurrency", method = RequestMethod.GET)
    @ResponseBody
    public String get1688APIUseCurrency(HttpServletRequest request) {
        User user = CurrentUserService.getCurrentUser();
        int size = checkProductStatusService.get1688APIUseCurrency(user.getUserName()).size();
        if (size > 0)
            return "error";
        return "success";
    }

    // 得到执行查询结果，返回到前台格网中的数据集合
    @RequestMapping(value = "/getSearchResult", method = RequestMethod.GET)
    @ResponseBody
    public void getSearchResult(HttpServletResponse response, HttpServletRequest request,
            @RequestParam("page") int page, @RequestParam("rows") int rows) throws Exception {
        String userName = CurrentUserService.getCurrentUser().getUserName();
        List<CheckProductStatus> result = checkProductStatusService.getResult(userName);
        JSONData.dealEndData(response, userName, page, rows, result);
    }

    /*
     * 文件上传完成之后检查Excel，如果有问题则删除上传的所有文件 当无文件并且无错误时返回null，有错误时返回错误列表list。
     */
    @RequestMapping(value = "/checkExcels", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<String> checkExcels(HttpServletRequest request) {
        String userName = CurrentUserService.getCurrentUser().getUserName();
        String path = ConfigInfo.rootUplaodPath + userName;
        int fileNum = FileUtils.getFileList(path).size();
        List<String> resultList = new ArrayList<>();
        if (fileNum > 0) {
            resultList = ExcelCheck.checkExcels(path);
            // 当有异常的时候需要清空本次导入
            if (resultList != null && resultList.size() > 0)
                FileUtils.clearDirectory(path);
        } else {
            resultList.add("0");// 没有文件，不需要清库，不需要导入。
            return resultList;
        }
        // 为null时说明没有错误不需要提示alert，需要清库。不为null时，需要alert，不要清库，要alert
        return resultList;
    }

    @RequestMapping(value = "/checkThread", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public boolean checkThread(HttpServletRequest request) {
        return CheckThreadJVM.isThreadRunning();

    }

}
