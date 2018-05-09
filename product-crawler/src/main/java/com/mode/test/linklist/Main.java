package com.mode.test.linklist;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkDefine link = new LinkDefine();
		LinkDefine head = null;
		NodeData node = new NodeData();
		node.name = "a";
		node.number = 1;
		link.addNode(head, node);
		head = link;
		link.addNode(head, node);

		System.out.println("11");
	}

}
