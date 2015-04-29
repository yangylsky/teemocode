package tk.teemocode.module.identity.index.convertor;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.base.convertor.BaseConvertor;
import tk.teemocode.module.identity.bo.User;
import tk.teemocode.module.identity.index.item.UserIndexItem;
import tk.teemocode.module.index.convertor.BaseIndexItemConvertor;

public class UserIndexConvertor extends BaseIndexItemConvertor<UserIndexItem, User> {
	@Override
	public UserIndexItem convert(User user) {
		UserIndexItem userIndexItem = super.convert(user);
		userIndexItem.setContact(BaseConvertor.convert(user.getContact(), Contact.class));
		return userIndexItem;
	}
}
