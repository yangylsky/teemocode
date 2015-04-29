package tk.teemocode.commons.component.tree;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.teemocode.commons.component.json.JSONHiEncoder;
import tk.teemocode.commons.util.reflect.DozerMapper;
import tk.teemocode.commons.util.reflect.PojoUtil;

public class JTreeRender {
	private static final Log log = LogFactory.getLog(JTreeRender.class);

	public static String renderJtreeToExtJson(JTree jtree) {
		if(jtree == null) {
			return "[]";
		}
		JNode jnode = jtree.getRoot();
		String code = renderJNodeToExtJson(jnode);
		return "[" + code + "]";
	}

	public static String renderJNodeToExtJson(JNode jnode) {
		if(jnode == null) {
			return "";
		}

		ExtNode node = renderJNodeToExtNode(jnode);
		if(node == null) {
			return "";
		}
		Map<String, Object> map = renderExtNode(node);

		return JSONHiEncoder.encode(map);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Map<String, Object> renderExtNode(ExtNode node) {
		Map<String, Object> map = renderExtNodeToMap(node, true);
		List<ExtNode> ns = node.getChildren();

		Collections.sort(ns, new Comparator() {
			@Override
			public int compare(Object ar1, Object ar2) {

				ExtNode obj1 = (ExtNode) ar1;
				ExtNode obj2 = (ExtNode) ar2;
				if(obj1.getSortIdx() == obj2.getSortIdx()) {
					return 0;
				}
				if(obj1.getSortIdx() < obj2.getSortIdx()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		for(ExtNode n : ns) {
			Map<String, Object> cMap = renderExtNode(n);
			childs.add(cMap);
		}

		if(childs.size() > 0) {
			map.put("children", childs);
		}
		return map;
	}

	private static Map<String, Object> renderExtNodeToMap(ExtNode node, boolean isCheckNull) {
		Map<String, Object> obj = new Hashtable<String, Object>();
		if(isCheckNull) {
			JTreeRender.objToMapForNotNull(node, obj);
		} else {
			PojoUtil.objToMap(node, obj);
		}
		return obj;
	}

	public static void objToMapForNotNull(Object obj, Map<String, Object> map) {
		if(map == null) {
			map = new HashMap<String, Object>();
		}
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(obj);
		for(PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			try {
				if(descriptor.getReadMethod() != null) {
					Object value = PropertyUtils.getProperty(obj, name);
					if(value != null) {
						if(value instanceof List) {
							continue;
						}

						map.put(name, PropertyUtils.getProperty(obj, name));
					}
				}
			} catch(NullPointerException e) {
			} catch(IllegalArgumentException e) {
			} catch(Exception e) {
				log.error(e);
			}
		}
	}

	public static ExtNode renderJNodeToExtNode(JNode jnode) {
		if(jnode == null) {
			return null;
		}
		if(jnode.getExport() != null && !jnode.getExport().booleanValue()) {
			return null;
		}

		ExtNode node = DozerMapper.getInstance().map(jnode, ExtNode.class);
		node.setText(jnode.getDescription());
		JNode[] jnodes = jnode.findChildren();
		for(JNode n : jnodes) {
			ExtNode en = renderJNodeToExtNode(n);
			if(en != null) {
				node.getChildren().add(en);
			}
		}

		return node;

	}

	public static void expandAll(JNode node, Boolean isExpand) {
		if(node != null) {
			node.setExpanded(isExpand);
			JNode[] ns = node.findChildren();
			for(JNode n : ns) {
				expandAll(n, isExpand);
			}
		}

	}

	public static void checkedAll(JNode node, Boolean checked) {
		if(node != null) {
			node.setChecked(checked);
			JNode[] ns = node.findChildren();
			for(JNode n : ns) {
				checkedAll(n, checked);
			}
		}

	}

}
