package tk.teemocode.module.identity.localservice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.module.base.service.impl.LocalServiceImpl;
import tk.teemocode.module.identity.bo.Module;
import tk.teemocode.module.identity.localservice.ModuleLocalService;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ModuleLocalServiceImpl extends LocalServiceImpl<Module> implements ModuleLocalService {

}
