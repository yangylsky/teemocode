package tk.teemocode.commons.component.tree;

public interface Tree {
	public Node getRoot();

	public Node findNode(String key);

	public void selectNode(String key);

	public void addNode(JNode node) throws IllegalArgumentException;

	public int getTreeWidth();

	/**
	 * 该节点到树底端的距离 与node.getDepth()区别： 后者是该节点到树顶的距离。
	 *
	 * @param node
	 * @return
	 */
	public int getWidth(JNode node);
}
