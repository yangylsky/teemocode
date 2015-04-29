package tk.teemocode.module.identity.localservice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.module.base.service.impl.LocalServiceImpl;
import tk.teemocode.module.identity.bo.Group;
import tk.teemocode.module.identity.localservice.GroupLocalService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class GroupLocalServiceImpl extends LocalServiceImpl<Group>  implements GroupLocalService {
}
