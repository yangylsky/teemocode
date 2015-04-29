package tk.teemocode.commons.component.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JNode implements Node, Serializable, Cloneable {
	private static final long serialVersionUID = 4410315246498160191L;

	// 是否是兄弟姐妹中最后一个
	private boolean last = false;

	// 是否是叶（无子节点）
	private boolean leaf = false;

	// 本Node是否被选择
	private boolean selected = false;

	// 是否展开显示其所有的子节点
	private boolean expanded = false;

	// 是否可编辑
	private boolean editable = false;

	// 该节点的父节点
	private JNode parent = null;

	// 该节点的深度
	private int depth = 0;

	// 树形结构对象
	private Tree tree = null;

	// 该节点的所有子节点
	private List<JNode> children = new ArrayList<JNode>();

	// 节点的关键字
	private String key = null;

	// 节点的label标识名称
	private String description = null;

	// 后台是否更新
	private boolean root = false;

	// 排序
	private int sortIdx = 0;

	// ---------------------------------节点在树结构中的属性
	// 扩展属性
	// 节点link
	private String href = null;

	// 节点link
	private String hrefTarget = null;

	// 节点图标 class
	private String iconCls = null;

	// 节点 class
	private String cls = null;

	// 节点tip
	private String qtip = null;

	// 节点tip配置
	private String qtipCfg = null;

	// Node checbox选中
	private Boolean checked;

	// 是禁用
	private Boolean disabled;

	// 是否输出Node;
	private Boolean export = true;

	Map<String, Object> attrs = new HashMap<String, Object>();

	public boolean isLast() {
		return (last);
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isLeaf() {
		return (leaf);
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void clearChildren() {
		children.clear();
	}

	/**
	 * Return the set of child nodes for this node.
	 */
	public JNode[] findChildren() {
		JNode results[] = new JNode[children.size()];
		return children.toArray(results);
	}

	/**
	 * 增加子节点
	 *
	 * @param child
	 * @throws java.lang.IllegalArgumentException
	 */
	public void addChild(JNode child) throws IllegalArgumentException {
		tree.addNode(child);
		child.setParent(this);
		leaf = false; // 一旦增加子节点，该节点就不是叶了。
		int n = children.size();
		if(n > 0) {
			JNode node = children.get(n - 1);
			node.setLast(false);
		}
		child.setLast(true);
		children.add(child);
	}

	public JNode getParent() {
		return (parent);
	}

	public void setParent(JNode parent) {
		this.parent = parent;
		if(parent == null) {
			depth = 0;
		} else {
			depth = parent.getDepth() + 1;
		}
	}

	public int getDepth() {
		return depth;
	}

	// --------------------------------- 节点在树结构中的属性　结束

	// --------------------------------- 与动态显示相关

	public boolean isExpanded() {
		return (expanded);
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	// --------------------------------- 与动态显示相关　结束

	// --------------------------------- 节点的通用属性

	public String getKey() {
		return (key);
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public String getDescription() {
		return description;
	}

	// --------------------------------- 节点的通用属性　结束

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

	public JNode(String key, String description, boolean leaf) {
		super();
		this.key = key;
		this.description = description;
		this.leaf = leaf;
	}

	public int getSortIdx() {
		return sortIdx;
	}

	public void setSortIdx(int sortIdx) {
		this.sortIdx = sortIdx;
	}

	public boolean isEditable() {
		return editable;
	}

	public String getHref() {
		return href;
	}

	public String getHrefTarget() {
		return hrefTarget;
	}

	public String getIconCls() {
		return iconCls;
	}

	public String getCls() {
		return cls;
	}

	public String getQtip() {
		return qtip;
	}

	public String getQtipCfg() {
		return qtipCfg;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	public void setQtipCfg(String qtipCfg) {
		this.qtipCfg = qtipCfg;
	}

	public boolean isSelected() {
		return selected;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean export) {
		this.export = export;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}
}
