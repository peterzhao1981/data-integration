package com.mode.test.orderlist;

class Data {
	String key = null;
	String name = null;
	int age = 0;
}

public class OrderLsit {
	static final int MAXLINE = 100;
	Data[] listData = new Data[MAXLINE + 1];
	int listLen;

	void olInit() {
		this.listLen = 0;
	}

	int olLength() {
		return this.listLen;
	}

	int olInsert(int n, Data data) {
		int i;
		if (this.listLen >= MAXLINE) {
			System.out.println("顺序表已满");
			return 0;
		}
		if (n < 1 || n > this.listLen - 1) {
			System.out.println("插入元素错误，无法插入");
			// return 0;
		}
		for (i = this.listLen; i > n; i--) {
			this.listData[i + 1] = this.listData[i];
		}
		this.listData[n] = data;
		this.listLen++;
		return 1;
	}

	// 增加元素到顺序表尾部
	int olAdd(Data data) {
		if (this.listLen >= MAXLINE) {
			System.out.println("顺序表已满，无法插入");
			return 1;
		}
		this.listData[++this.listLen] = data;
		return 1;
	}

}
