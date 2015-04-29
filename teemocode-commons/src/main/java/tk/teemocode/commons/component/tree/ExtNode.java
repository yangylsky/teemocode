package tk.teemocode.commons.component.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtNode {
	// 节点的关键字
	private String key = null;

	// 节点的显示
	private String text = null;

	// 是否是叶（无子节点）
	private Boolean leaf = false;

	// 是否可编辑
	private Boolean editable = null;

	// 是否禁用
	private Boolean disabled = null;

	// 是否展开显示其所有的子节点
	private Boolean expanded = null;

	// 本Node是否被选择
	private Boolean checked = null;

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

	// 该节点的深度
	private int depth = 0;

	// 排序
	private int sortIdx = 0;

	private List<ExtNode> children = new ArrayList<ExtNode>();

	//
	// //扩展属性
	private Map<String, Object> attrs = new HashMap<String, Object>();

	public String getKey() {
		return key;
	}

	public String getText() {
		return text;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public int getDepth() {
		return depth;
	}

	public List<ExtNode> getChildren() {
		return children;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setChildren(List<ExtNode> children) {
		this.children = children;
	}

	public Boolean getEditable() {
		return editable;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public Boolean getChecked() {
		return checked;
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

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
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

	public int getSortIdx() {
		return sortIdx;
	}

	public void setSortIdx(int sortIdx) {
		this.sortIdx = sortIdx;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

}
