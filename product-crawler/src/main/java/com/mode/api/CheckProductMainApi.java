package com.mode.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.service.CheckProductStatusService;

/**
 * Created by zhaoweiwei on 2017/11/13.
 */
@RestController
@RequestMapping(value = "/checkProduct")
public class CheckProductMainApi {

    @Autowired
    private CheckProductStatusService checkProductStatusService;

    // 1、每一次开始新的爬虫之前，需要清库，或者手动将表删掉。启动程序即可重新建表
    @RequestMapping(value = "/excelProcess", method = RequestMethod.GET)
    public void excelProcess() throws Exception {
        checkProductStatusService.excelProcess();
        System.out.println("导入excel至数据库成功");
    }

    // 2、增量式爬取，如果记录不为error url,lack将重新进行爬取。可能要调用该url多次
    @RequestMapping(value = "/checkProductStatus", method = RequestMethod.GET)
    public void checkProductStatus() throws Exception {
        checkProductStatusService.process();
    }

    // 3、导出数据库数据到excel中,只导出url无效的excel
    @RequestMapping(value = "/exportDBtoExcelLessInfo", method = RequestMethod.GET)
    public void exportDBtoExcelLessInfo() {
        try {
            checkProductStatusService.exportDBtoExcel(1);
            System.out.println("导出数据库数据至：" + ConfigInfo.outPutExcel + "：成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("导出数据库数据至失败");
        }

    }

    // 3、导出数据库数据到excel中,只导出url无效的excel
    @RequestMapping(value = "/exportDBtoExcelMoreInfo", method = RequestMethod.GET)
    public void exportDBtoExcelMoreInfo() {
        try {
            checkProductStatusService.exportDBtoExcel(2);
            System.out.println("导出数据库数据至：" + ConfigInfo.outPutExcel + "：成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导出数据库失败");
        }

    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getInfo(ModelAndView mv, HttpServletRequest request) throws Exception {
        mv.setViewName("/mainPage");
        mv.addObject("title", "欢迎使用Thymeleaf!");
        return mv;
    }

    public static void main(String[] args) {

    }
}
