package com.mode.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mode.entity.Order;
import com.mode.repository.OrderRepository;
import com.mode.service.DxmProductNameUpdateService;
import com.mode.service.NewSkuGenService;
import com.mode.service.ProductCrawlerServiceImpl;
import com.mode.service.PurchaseService;
import com.mode.service.StatementService;
import com.mode.service.TempPurchaseCommentGenService;
import com.mode.service.TestService;

/**
 * Created by zhaoweiwei on 2017/11/13.
 */
@RestController
public class MainApi {

    @Autowired
    private ProductCrawlerServiceImpl productCrawlerServiceImpl;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StatementService statementService;

    @Autowired
    private TestService testService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private NewSkuGenService newSkuGenService;

    @Autowired
    private DxmProductNameUpdateService dxmProductNameUpdateService;

    @Autowired
    private TempPurchaseCommentGenService tempPurchaseCommentGenService;



//    @RequestMapping(value = "/init", method = RequestMethod.POST)
//    public void initProducts() {
//        productCrawlerServiceImpl.initProducts();
//    }
//
//    @RequestMapping(value = "/crawler", method = RequestMethod.POST)
//    public void crawler() {
//        productCrawlerServiceImpl.processWhatsmode();
//    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
//        Order order = orderRepository.getOne(2l);
//        System.out.println(order.getOrderNo());
//        System.out.println(order.getOrderStatus());
//        System.out.println(order.getPlatform());
//        System.out.println(order.getSku());
//        System.out.println(order.getProductName());
//        System.out.println(order.getCustomerServiceComment());
//        System.out.println(order.getOrderedAt());
//        System.out.println(order.getSkuDetail());
//        System.out.println(order.getParcelNo());
//        System.out.println(order.getSkuFactoryPrice());
//
//        String s = order.getSku();
//        String[] ss = s.split("\r");
//        for (String sss : ss) {
//            System.out.println(sss);
//        }
//        statementService.updatePercentage();
        testService.test();
        return "test";
    }

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public void process() {
        statementService.process();
    }


    @RequestMapping(value = "/purchase", method = RequestMethod.GET)
    public void purchase() {
        purchaseService.process();
    }

    @RequestMapping(value = "/skuGen", method = RequestMethod.GET)
    public void skuGen() {
        newSkuGenService.process();
    }

    @RequestMapping(value = "/commentGen", method = RequestMethod.GET)
    public void commentGen() {
        tempPurchaseCommentGenService.process();
    }

    @RequestMapping(value = "/productNameGen", method = RequestMethod.GET)
    public void productNameUpdate() {
        dxmProductNameUpdateService.process();
    }

    public static void main(String[] args) {
//        String s = "25.0\n" +
//                "23.0\n" +
//                "26.0\n" +
//                "0.0\n" +
//                "19.0";
        String s = "76.0";
        String[] ss = s.split("\n");
        System.out.println(ss.length);

//        String s1 = "14380*1\n" +
//                "14016*1\n" +
//                "9701*1\n" +
//                "13431*1\n" +
//                "13807*1";
        String s1 = "13747*1";
        String[] ss1 = s1.split("\n");
        System.out.println(ss1.length);
        for(String sss1 : ss1) {
            System.out.println(sss1);
        }

        String detail = "14380*1";
        String[] a = detail.split("\\*");
        System.out.println(a[0]);
        System.out.println(a[1]);
    }
}
