package tk.teemocode.module.identity.index.item;

import org.apache.commons.lang3.StringUtils;

import tk.teemocode.commons.util.reflect.PojoUtil;
import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.identity.bo.Group;
import tk.teemocode.module.index.EmbedItem;
import tk.teemocode.module.util.CommonUtil;

public class GroupEmbedItem extends EmbedItem {
	private String name;

	private String no;

	private String description;

	private Contact contact;

	public GroupEmbedItem() {
	}

	public GroupEmbedItem(Group group) {
		PojoUtil.merge(this, group);
		PojoUtil.merge(this.contact, group.getContact());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	//==========================Business Method==============================//

	public static GroupEmbedItem getGroupEmbedItem(String groupUuid) {
		if(StringUtils.isBlank(groupUuid)) {
			return null;
		}
		Group group = CommonUtil.getBoByUuid(Group.class, groupUuid);
		return new GroupEmbedItem(group);
	}
}
