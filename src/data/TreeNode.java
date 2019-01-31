package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeNode { // this will not be generic to only store x,y primitive coordinates instead of
						// objects representing moves

	private TreeNode parent = null;
	private List<TreeNode> children = null;
	private int x1, y1, x2, y2;
	private int successfulPaths = 0;

	public TreeNode(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		children = new ArrayList<>();
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getSuccessfulPaths() {
		return successfulPaths;
	}

	public void setSuccessfulPaths(int successfulPaths) {
		this.successfulPaths = successfulPaths;
	}

	public void addSuccessfulPaths(int successfulPaths) {
		this.successfulPaths += successfulPaths;
	}

	public void addChild(TreeNode node) {
		node.setParent(this);
		children.add(node);
	}

	public TreeNode getChild(int index) {
		if (children == null || children.size() <= index) {
			return null;
		}
		return children.get(index);
	}

	public void printTreeDepthFirstSearch(TreeNode root) {
		System.out.println(root.getX1() + "," + root.getY1() + " " + root.getX2() + "," + root.getY2());
		if (root.getChildren() != null) {
			for (TreeNode child : root.getChildren()) {
				printTreeDepthFirstSearch(child);
			}
		}
	}

	public void printTreeBreadthFirstSearch(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		TreeNode traverseNode;
		while (!(queue.isEmpty())) {
			traverseNode = queue.poll();
			if (traverseNode.getChildren() != null) {
				for (TreeNode node : traverseNode.getChildren()) {
					queue.add(node);
				}
			}
			System.out.println(traverseNode.getX1() + "," + traverseNode.getY1() + " " + traverseNode.getX2() + ","
					+ traverseNode.getY2());
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TreeNode)) {
			return false;
		}
		TreeNode otherNode = (TreeNode) other;
		return this.x1 == otherNode.x1 && this.y1 == otherNode.y1 && this.x2 == otherNode.x2 && this.y2 == otherNode.y2;
	}

	@Override
	public String toString() {
		return x1 + " " + y1 + " " + x2 + " " + y2 + " succPaths: " + successfulPaths;
	}
}