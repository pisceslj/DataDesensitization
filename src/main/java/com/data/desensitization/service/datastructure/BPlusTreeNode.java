package com.data.desensitization.service.datastructure;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;;

public class BPlusTreeNode <Key extends Comparable<Key>, Value extends Comparable<Value>> {
	// parent node
	protected BPlusTreeNode<Key, Value> parent;
	
	// the previous node of the leaf node
	protected BPlusTreeNode<Key, Value> previous;
	
	// the next node of the leaf node
	protected BPlusTreeNode<Key, Value> next;
	
	// the key nodes
	protected List<Entry<Key, Value>> entries;
	
	// the children nodes
	protected List<BPlusTreeNode<Key, Value>> children;
	
	// true if the node is leaf
	protected boolean isLeaf;
	
	// true if the node is root
	protected boolean isRoot;
	
	public BPlusTreeNode(boolean isLeaf) {
		this.isLeaf  = isLeaf;
		entries = new ArrayList<Entry<Key, Value>>();
		
		if (!isLeaf) {
			children = new ArrayList<BPlusTreeNode<Key, Value>>();
		}
	}
	
	public BPlusTreeNode(boolean isLeaf, boolean isRoot) {
		this(isLeaf);
		this.isRoot = isRoot;
	}
	
	public Value get(Key key) {
		// if it is leaf node
		if (isLeaf) {
			int low = 0, high = entries.size() - 1, mid;
			int compareL, compareM, compareH;
			while (low <= high) {
				mid = (low + high) / 2;
				if (mid == 0) {
					return entries.get(mid).getValue();
				}
				if (mid == high) {
					return entries.get(mid).getValue();
				}
				//System.out.println("size:" + entries.size() + "mid:" + mid);
				compareL = entries.get(mid-1).getKey().compareTo(key);
				compareM = entries.get(mid).getKey().compareTo(key);
				compareH = entries.get(mid+1).getKey().compareTo(key);
				// mid-1 < key < mid || mid < key < mid + 1
				if (compareL < 0 && compareM >= 0 || compareM < 0 && compareH >= 0) {
					return entries.get(mid).getValue();  // return the index if match 
				} else if (compareH < 0) {
					low = mid + 1;
				} else {
					high = mid - 1;
				}
			}
			return null; // can't find the match node
		}
		// if it is not leaf node
		if (key.compareTo(entries.get(0).getKey()) < 0) {
			return children.get(0).get(key);
			// if the key is bigger than the right key of the node, search alone the last node
		} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
			return children.get(children.size() - 1).get(key);
			// or search alone the node which is bigger than current node
		} else {
			int low = 0, high = entries.size() - 1, mid;
			int compareL, compareM, compareH;
			while (low <= high) {
				mid = (low + high) / 2;
				if (mid == 0) {
					return children.get(mid+1).get(key);
				}
				compareL = entries.get(mid - 1).getKey().compareTo(key);
				compareM = entries.get(mid).getKey().compareTo(key);
				compareH = entries.get(mid + 1).getKey().compareTo(key);
				if (compareL < 0 && compareM >= 0 || compareM < 0 && compareH >= 0) {
					return children.get(mid + 1).get(key);  // return the index if match 
				} else if (compareH < 0) {
					low = mid + 1;
				} else {
					high = mid - 1;
				}
			}
			return children.get(low).get(key);
		}
	}
	
	public void insert(Key key, Value value, BPlusTree<Key, Value> tree) {
		// if current node is leaf node
		if (isLeaf) {
			// don't need to split, insert directly
			if (contains(key) != -1 || entries.size() < tree.getOrder()) {
				insert(key, value);
				if (tree.getHeight() == 0) {
					tree.setHeight(1);
				}
				return;
			}
			// need to split into two nodes:left node and right node
			BPlusTreeNode<Key, Value> left = new BPlusTreeNode<Key, Value>(true);
			BPlusTreeNode<Key, Value> right = new BPlusTreeNode<Key, Value>(true);
			// set the reference
			if (previous != null) {
				previous.next = left;
				left.previous = previous;
			}
			if (next != null) {
				next.previous = right;
				right.next = next;
			}
			if (previous == null) {
				tree.setHead(left);
			}
			
			left.next = right;
			right.previous = left;
			previous = null;
			next = null;
			// copy the old keys of node to the split two nodes
			copyToNodes(key, value, left, right, tree);
			
			// if not root node
			if (parent != null) {
				// change the relation of parent and current nodes
				int index = parent.children.indexOf(this);
				parent.children.remove(this);
				left.parent = parent;
				right.parent = parent;
				parent.children.add(index,left);
				parent.children.add(index + 1, right);
				parent.entries.add(index,right.entries.get(0));
				entries = null; // delete the key information of the current node
				children = null; // delete the reference of the current node's children
				
				// insert into the parent node
				parent.updateInsert(tree);
				parent = null;
				// if the current node is root
			} else {
				isRoot = false;
				BPlusTreeNode<Key,Value> parent = new BPlusTreeNode<Key,Value> (false, true);
				tree.setRoot(parent);
				left.parent = parent;
				right.parent = parent;
				parent.children.add(left);
				parent.children.add(right);
				parent.entries.add(right.entries.get(0));
				entries = null;
				children = null;
			}
			return;
		}
		// if not the leaf node
		if (key.compareTo(entries.get(0).getKey()) < 0) {
			children.get(0).insert(key, value, tree);
		// if key is bigger the right node's key, search alone the last node
		} else if (key.compareTo(entries.get(entries.size()-1).getKey()) >= 0) {
			children.get(children.size()-1).insert(key, value, tree);
		// or search the node which is bigger than curent node
		} else {
			int low = 0, high = entries.size() - 1, mid= 0;
			int compare;
			if (key.compareTo(entries.get(0).getKey()) < 0) {
				while (low <= high) {
					mid = (low + high) / 2;
					compare = entries.get(mid).getKey().compareTo(key);
					if (compare == 0) {
						children.get(mid+1).insert(key, value, tree);
						break;
					} else if (compare < 0) {
						low = mid + 1;
					} else {
						high = mid - 1;
					}
				}
				if (low > high) {
					children.get(low).insert(key, value, tree);
				}
			}
		}
	}
	
	private void copyToNodes(Key key, Value value, BPlusTreeNode<Key, Value> left,
			BPlusTreeNode<Key, Value> right, BPlusTree<Key, Value> tree) {
		// get the length of the left and right keys
		int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
		boolean b = false; // record if the new node is already inserted
		for (int i = 0; i < entries.size(); i++) {
			if(leftSize !=0) {
				leftSize--;
				if(!b&&entries.get(i).getKey().compareTo(key) > 0) {
					left.entries.add(new SimpleEntry<Key, Value>(key, value));
					b = true;
					i--;
				} else {
					left.entries.add(entries.get(i));
				}
			} else {
				if(!b&&entries.get(i).getKey().compareTo(key) > 0) {
					right.entries.add(new SimpleEntry<Key, Value>(key, value));
					b = true;
					i--;
				} else {
					right.entries.add(entries.get(i));
				}
			}
		}
		if (!b) {
			right.entries.add(new SimpleEntry<Key, Value>(key, value));
		}
	}
	
	// uodate the node
	private void updateInsert(BPlusTree<Key, Value> tree) {
		//如果子节点数超出阶数，则需要分裂该节点
		if (children.size() > tree.getOrder()) {
			//分裂成左右两个节点
			BPlusTreeNode<Key, Value> left = new BPlusTreeNode<Key, Value>(false);
			BPlusTreeNode<Key, Value> right = new BPlusTreeNode<Key, Value>(false);
			//左右两个节点子节点的长度
			int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
			int rightSize = (tree.getOrder() + 1) / 2;
			//复制子节点到分裂出来的新节点，并更新关键字
			for (int i = 0; i < leftSize; i++){
				left.children.add(children.get(i));
				children.get(i).parent = left;
			}
			for (int i = 0; i < rightSize; i++) {
				right.children.add(children.get(leftSize + i));
				children.get(leftSize + i).parent = right;
			}
			for (int i = 0; i < leftSize - 1; i++) {
				left.entries.add(entries.get(i));
			}
			for (int i = 0; i < rightSize - 1; i++) {
				right.entries.add(entries.get(leftSize + i));
			}	
			//如果不是根节点
			if (parent != null) {
				//调整父子节点关系
				int index = parent.children.indexOf(this);
				parent.children.remove(this);
				left.parent = parent;
				right.parent = parent;
				parent.children.add(index,left);
				parent.children.add(index + 1, right);
				parent.entries.add(index,entries.get(leftSize - 1));
				entries = null;
				children = null;
			
				//父节点更新关键字
				parent.updateInsert(tree);
				parent = null;
				//如果是根节点
			} else {
				isRoot = false;
				BPlusTreeNode<Key, Value> parent = new BPlusTreeNode<Key, Value>(false, true);
				tree.setRoot(parent);
				tree.setHeight(tree.getHeight() + 1);
				left.parent = parent;
				right.parent = parent;
				parent.children.add(left);
				parent.children.add(right);
				parent.entries.add(entries.get(leftSize - 1));
				entries = null;
				children = null;
			}
		}
	}
	
	// if the current node meet the value 
	private int contains(Key key) {
		int low = 0, high = entries.size() - 1, mid;
		int compare ;
		while (low <= high) {
			mid = (low + high) / 2;
			compare = entries.get(mid).getKey().compareTo(key);
			if (compare == 0) {
				return mid;
			} else if (compare < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return -1;
	}
	
	private void insert(Key key, Value value) {
		// binary search
		int low = 0, high = entries.size() - 1, mid;
		int compare ;
		while (low <= high) {
			mid = (low + high) / 2;
			compare = entries.get(mid).getKey().compareTo(key);
			if (compare == 0) {
				entries.get(mid).setValue(value);
				break;
			} else if (compare < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		if(low > high) {
			entries.add(low, new SimpleEntry<Key, Value>(key, value));
		}
	}
}
