package com.mode.test.orderlist;

public class Main {
	public static void main(String[] args) {
		OrderLsit orderLsit = new OrderLsit();
		Data data = new Data();
		data.age = 1;
		data.name = "a";
		data.key = "1";
		orderLsit.olInit();
		System.out.println("初始化顺序表完成");

		orderLsit.olAdd(data);
		data.age = 2;
		data.name = "b";
		data.key = "2";
		orderLsit.olAdd(data);

		for (Data dataIt : orderLsit.listData) {
			if (dataIt == null) {
				continue;
			}
			System.out.print(dataIt.age);
			System.out.println();

		}
	}

}
