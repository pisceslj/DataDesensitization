package com.data.desensitization.service.datastructure;

public class BPlusTree <Key extends Comparable<Key>, Value extends Comparable<Value>> {
	protected int order; // order
	
	protected BPlusTreeNode<Key, Value> root; // root node
	protected BPlusTreeNode<Key, Value> head; // head of leaves nodes
	
	protected int height = 0;
	
	// BPlus Tree Node
	public BPlusTreeNode<Key, Value> getHead() {
		return head;
	}
	
	public void setHead(BPlusTreeNode<Key, Value> head) {
		this.head = head;
	}
	
	public BPlusTreeNode<Key, Value> getRoot() {
		return root;
	}
	
	public void setRoot(BPlusTreeNode<Key, Value> root) {
		this.root = root;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public Value get(Key key) {
		return root.get(key);
	}
	
	public void insert(Key key, Value value) {
		root.insert(key, value, this);
	}
	
	public BPlusTree(int order) {
		this.order = order;
		root = new BPlusTreeNode<Key, Value>(true, true);
		head = root;
	}
}
