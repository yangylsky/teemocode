package tk.teemocode.module.identity.localservice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.module.base.service.impl.LocalServiceImpl;
import tk.teemocode.module.identity.bo.Role;
import tk.teemocode.module.identity.localservice.RoleLocalService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class RoleLocalServiceImpl extends LocalServiceImpl<Role> implements RoleLocalService {

}
