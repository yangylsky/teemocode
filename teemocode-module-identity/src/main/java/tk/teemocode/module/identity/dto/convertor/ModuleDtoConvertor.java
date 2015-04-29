package tk.teemocode.module.identity.dto.convertor;

import tk.teemocode.module.base.convertor.BaseDtoConvertor;
import tk.teemocode.module.identity.bo.Module;
import tk.teemocode.module.identity.dto.ModuleDto;

public class ModuleDtoConvertor extends BaseDtoConvertor<ModuleDto, Module> {
	public static final ModuleDtoConvertor INSTANCE = new ModuleDtoConvertor();

	private ModuleDtoConvertor() {
	}

	@Override
	public ModuleDto convert(Module module, String... fields) {
		ModuleDto moduleDto = super.convert(module, fields);
		String parent = module.getParent() == null ? null : module.getParent().getUuid();
		moduleDto.setParent(parent);
		return moduleDto;
	}
}
