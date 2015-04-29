package tk.teemocode.module.identity.localservice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.module.base.service.impl.LocalServiceImpl;
import tk.teemocode.module.identity.bo.Authority;
import tk.teemocode.module.identity.localservice.AuthorityLocalService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class AuthorityLocalServiceImpl extends LocalServiceImpl<Authority> implements AuthorityLocalService {

}
