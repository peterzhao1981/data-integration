package com.mode.test.linklist;

class NodeData {
	public int number;
	public String name;
}

public class LinkDefine {
	NodeData nodeData = new NodeData();
	LinkDefine nextLink;

	int addNode(LinkDefine head, NodeData node) {
		LinkDefine linkDefine = new LinkDefine(), htemp;
		linkDefine.nodeData = node;
		linkDefine.nextLink = null;
		if (head == null) {
			head = linkDefine;
			return 1;
		}
		htemp = head;
		while (htemp.nextLink != null) {
			htemp = htemp.nextLink;
		}
		htemp.nextLink = linkDefine;
		return 2;
	}
}
