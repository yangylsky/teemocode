package tk.teemocode.module.identity.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.module.base.service.impl.BusinessServiceImpl;
import tk.teemocode.module.identity.bo.Role;
import tk.teemocode.module.identity.localservice.RoleLocalService;
import tk.teemocode.module.identity.service.RoleService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class RoleServiceImpl extends BusinessServiceImpl implements RoleService {
	@Resource
	private RoleLocalService roleLocalService;

	@Override
	public List<String> getRoleAuths(String uuid) {
		Role role = roleLocalService.getByUuid(uuid);
		if(role != null) {
			return role.getAuthUuids();
		} else {
			return new ArrayList<String>();
		}
	}
}
