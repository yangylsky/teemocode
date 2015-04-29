package tk.teemocode.module.identity.dto.convertor;

import org.apache.commons.lang.ArrayUtils;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.base.convertor.BaseConvertor;
import tk.teemocode.module.base.convertor.BaseDtoConvertor;
import tk.teemocode.module.identity.bo.User;
import tk.teemocode.module.identity.dto.UserDto;

public class UserDtoConvertor extends BaseDtoConvertor<UserDto, User> {
	public static final UserDtoConvertor INSTANCE = new UserDtoConvertor();

	private UserDtoConvertor() {
	}

	@Override
	public UserDto convert(User user, String... fields) {
		UserDto userDto = super.convert(user, fields);
		if(fields == null || ArrayUtils.contains(fields, "contact")) {
			Contact contact = BaseConvertor.convert(user.getContact(), Contact.class);
			userDto.setContact(contact);
		}
		return userDto;
	}

	@Override
	public User reverse(UserDto userDto, String... fields) {
		User user = super.reverse(userDto, fields);
		if(fields == null || ArrayUtils.contains(fields, "contact")) {
			Contact contact = BaseConvertor.convert(userDto.getContact(), Contact.class);
			user.setContact(contact);
		}
		return user;
	}
}
