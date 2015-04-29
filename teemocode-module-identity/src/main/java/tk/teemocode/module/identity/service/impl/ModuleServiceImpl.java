package tk.teemocode.module.identity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.component.json.JSONDecoder;
import tk.teemocode.commons.component.tree.JNode;
import tk.teemocode.commons.component.tree.JTree;
import tk.teemocode.commons.exception.NoSuchObjectException;
import tk.teemocode.module.base.service.impl.BusinessServiceImpl;
import tk.teemocode.module.identity.bo.Module;
import tk.teemocode.module.identity.dto.ModuleDto;
import tk.teemocode.module.identity.dto.convertor.ModuleDtoConvertor;
import tk.teemocode.module.identity.localservice.ModuleLocalService;
import tk.teemocode.module.identity.service.ModuleService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ModuleServiceImpl extends BusinessServiceImpl implements ModuleService {
	@Resource
	private ModuleLocalService moduleLocalService;

	@SuppressWarnings("unchecked")
	@Override
	protected ModuleDtoConvertor getDtoConvertor() {
		return ModuleDtoConvertor.INSTANCE;
	}

	@Override
	@Transactional(readOnly = true)
	public JTree getModuleTree(boolean hasDisable) {
		JTree jtree = null;
		try {
			JNode root = new JNode("module-root", "root", false);
			jtree = new JTree(root);
			List<ModuleDto> ms = getAllModule();
			for(ModuleDto m : ms) {
				if(StringUtils.isBlank(m.getParent()) && !StringUtils.isBlank(m.getUuid())) {
					buildTree(ms, jtree, root, m, hasDisable);
					// ms.remove(m);
				}
			}
		} catch(Exception e) {
			logger.error("", e);
			jtree = null;
		}

		return jtree;
	}

	public List<ModuleDto> getAllModule() {
		List<Module> list = moduleLocalService.getAll();
		return getDtoConvertor().convert(list);
	}

	private JTree buildTree(List<ModuleDto> infoList, JTree jTree, JNode parent, final ModuleDto model, boolean hasDisable) {

		if(model == null) {
			throw new NoSuchObjectException("模块不能为空.");
		}
		if(!hasDisable && model.getDisabled() == true) {
			return jTree;
		}
		List<ModuleDto> models = new ArrayList<ModuleDto>();
		for(ModuleDto m : infoList) {
			if(!StringUtils.isBlank(m.getParent()) && m.getParent().equals(model.getUuid())) {
				models.add(m);
				// infoList.remove(m);
			}
		}
		boolean isLeaf = models.size() == 0;
		JNode child = null;
		child = new JNode(model.getUuid(), model.getDescription(), isLeaf);
		if(parent != null) {
			parent.addChild(child);
		}

		child.setSortIdx(model.getSortIdx());
		try {
			String propsStr = model.getProps();
			if(!StringUtils.isBlank(propsStr)) {
				Map<String, Object> props = JSONDecoder.decode(propsStr);
				child.setAttrs(props);
			}
		} catch(Exception e) {
			logger.debug("参数格式错误!", e);
		}
		child.setAttribute("moduleName", model.getName());
		child.setAttribute("moduleUrl", model.getUrl());
		child.setDisabled(model.getDisabled());

		for(ModuleDto gChild : models) {
			buildTree(infoList, jTree, child, gChild, hasDisable);
		}
		return jTree;
	}

	@Override
	@Transactional(readOnly = true)
	public JNode getModuleTreeNode(String uuid, boolean hasDisable) {
		JTree tree = getModuleTree(hasDisable);
		return tree.findNode(uuid);
	}

	@Override
	public JNode getModuleTreeNodeByName(String name, boolean hasDisable) {
		Module model = moduleLocalService.getUniqueByProperty("name", name);
		if(model != null) {
			return getModuleTreeNode(model.getUuid(), hasDisable);
		}

		return null;
	}
}
