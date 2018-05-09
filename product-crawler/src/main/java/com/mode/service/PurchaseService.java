package com.mode.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.entity.DxmProduct;
import com.mode.entity.Order;
import com.mode.entity.Purchase;
import com.mode.repository.DxmProductRepository;
import com.mode.repository.OrderRepository;
import com.mode.repository.PurchaseFinishedRepository;
import com.mode.repository.PurchaseRepository;
import com.mode.util.RawDataUtil;

/**
 * Created by zhaoweiwei on 2017/12/5.
 */
@Service
public class PurchaseService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PurchaseFinishedRepository purchaseFinishedRepository;

	@Autowired
	private DxmProductRepository dxmProductRepository;

	public void process() {
		// List<PurchaseFinished> purchaseFinishedList =
		// purchaseFinishedRepository.findAll();
		List<String> orderNoList = new ArrayList<>();

		// for (PurchaseFinished purchaseFinished : purchaseFinishedList) {
		// orderNoList.add(purchaseFinished.getOrderNo().trim());
		// }
		// List<Order> orderList =
		// orderRepository.findByOrderNoNotIn(orderNoList);
		System.out.println("process");
		RawDataUtil.processLine("old_order.txt", line -> {

			if (StringUtils.isEmpty(line)) {
				return;
			}
			orderNoList.add(line);
		});

		List<Order> orderList = orderRepository.findByOrderNoIn(orderNoList);

		for (Order order : orderList) {
			String skuName = order.getSkuName();
			String skuDetail = order.getSkuDetail();
			String productName = order.getProductName();
			if (!StringUtils.isEmpty(skuName) && !StringUtils.isEmpty(skuDetail) && !StringUtils.isEmpty(productName)) {
				String[] skuNameList = skuName.split("\r");
				String[] skuDetailList = skuDetail.split("\r");
				String[] productNameList = productName.split("\r");
				if (skuNameList.length == skuDetailList.length) {
					for (int i = 0; i < skuNameList.length; i++) {
						String detail = skuDetailList[i];
						String[] str = detail.split("\\*");
						if (str.length == 2) {
							String sku = str[0];
							String quantity = str[1];
							Purchase purchase = new Purchase();
							purchase.setOrderNo(order.getOrderNo());
							purchase.setSku(sku);
							purchase.setSkuQuantity(quantity);

							purchase.setSkuName(skuNameList[i]);
							DxmProduct dxmProduct = dxmProductRepository.findOneBySku(sku);
							if (dxmProduct != null) {
								purchase.setSourceUrl(dxmProduct.getSourceUrl());
								purchase.setProductName(dxmProduct.getProductName());
							}
							purchaseRepository.save(purchase);
						} else {
							System.out.println("Error processing order_no -> " + order.getOrderNo());
						}
					}
				} else {
					System.out.println("Error processing order_no -> " + order.getOrderNo());
				}
			}
		}
	}

	public static void main(String[] args) {
		RawDataUtil.processLine("old_order.txt", line -> {

			if (StringUtils.isEmpty(line)) {
				return;
			}
			System.out.println(line);
		});
	}
}
