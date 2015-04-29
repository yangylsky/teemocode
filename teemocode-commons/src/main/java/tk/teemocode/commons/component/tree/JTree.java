package tk.teemocode.commons.component.tree;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class JTree implements Tree, Serializable, Cloneable {
	private static final long serialVersionUID = 1061134915842053502L;

	public static boolean init = true; // 是否初始化

	// 当前选中的节点
	private JNode selected = null;

	private JNode root = null;

	private Map<String, JNode> registry = new Hashtable<String, JNode>();

	private int sortIdx = 0;// 多tree排序

	private String dataUrl = null;// 数据url;

	// 扩展属性
	private Map<String, Object> attrs = new Hashtable<String, Object>();

	public JTree(JNode root) {
		this.root = root;
		root.setTree(this);
		root.setLast(true);
		registry.put(root.getKey(), root);
	}

	@Override
	public JNode getRoot() {
		return (root);
	}

	@Override
	public JNode findNode(String key) {
		return registry.get(key);
	}

	@Override
	public void selectNode(String key) {
		if(selected != null) {
			selected.setSelected(false);
			selected = null;
		}
		selected = findNode(key);
		if(selected != null) {
			selected.setSelected(true);
		}

	}

	@Override
	public void addNode(JNode node) throws IllegalArgumentException {
		node.setTree(this);
		registry.put(node.getKey(), node);
	}

	@Override
	public int getTreeWidth() {
		if(root == null) {
			return (0);
		} else {
			return (getWidth(root));
		}

	}

	/**
	 * 该节点到树底端的距离 与node.getDepth()区别： 后者是该节点到树顶的距离。
	 *
	 * @param node
	 * @return
	 */
	@Override
	public int getWidth(JNode node) {
		int width = node.getDepth();
		JNode children[] = node.findChildren();
		for(JNode element : children) {
			int current = getWidth(element);
			if(current > width) {
				width = current;
			}
		}
		return (width);
	}

	/**
	 * Get attribute of this node with specified attribute name
	 *
	 * @param name
	 *            The attribute name
	 * @return the attribute in <code>String</code>
	 */
	public Object getAttribute(String name) {
		return attrs.get(name);
	}

	/**
	 * Set attribute to this node with specified attribute name
	 *
	 * @param name
	 *            The attribute name
	 * @param value
	 *            The attribute
	 */
	public void setAttribute(String name, String value) {
		if(value != null) {
			attrs.put(name, value);
		}
	}

	/**
	 * Returns all attribute names in a <code>Iterator</code>
	 *
	 * @return
	 */
	public Iterator<String> getAttributeNames() {
		return attrs.keySet().iterator();
	}

	/**
	 * Clear the node.
	 */
	public void clearAttributes() {
		attrs.clear();
	}

	public int getSortIdx() {
		return sortIdx;
	}

	public void setSortIdx(int sortIdx) {
		this.sortIdx = sortIdx;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
}
