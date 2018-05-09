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
			System.out.println("˳�������");
			return 0;
		}
		if (n < 1 || n > this.listLen - 1) {
			System.out.println("����Ԫ�ش����޷�����");
			// return 0;
		}
		for (i = this.listLen; i > n; i--) {
			this.listData[i + 1] = this.listData[i];
		}
		this.listData[n] = data;
		this.listLen++;
		return 1;
	}

	// ����Ԫ�ص�˳���β��
	int olAdd(Data data) {
		if (this.listLen >= MAXLINE) {
			System.out.println("˳����������޷�����");
			return 1;
		}
		this.listData[++this.listLen] = data;
		return 1;
	}

}
