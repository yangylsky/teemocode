package tk.teemocode.module.identity.service;

import tk.teemocode.commons.component.tree.JNode;
import tk.teemocode.commons.component.tree.JTree;

public interface ModuleService {
	/**
	 * @param hasDisable
	 *            是否包含Disable的节点
	 * @return
	 */
	public JTree getModuleTree(boolean hasDisable);

	/**
	 * 获取功能节点
	 *
	 * @param uuid
	 *            查询的节点uuid
	 * @param hasDisable
	 *            是否包含Disable的节点
	 * @return
	 */
	public JNode getModuleTreeNode(String uuid, boolean hasDisable);

	/**
	 * 获取功能节点
	 *
	 * @param name
	 *            查询的节点name
	 * @param hasDisable
	 *            是否包含Disable的节点
	 * @return
	 */
	public JNode getModuleTreeNodeByName(String name, boolean hasDisable);
}
