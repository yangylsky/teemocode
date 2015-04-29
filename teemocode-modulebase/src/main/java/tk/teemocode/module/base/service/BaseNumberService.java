package tk.teemocode.module.base.service;

import tk.teemocode.module.base.dto.Dto;

public interface BaseNumberService {
	
	/**
	 * 参考系统编码对照表
	 * @return 编码分类
	 */
	public String getNumberType(int type);
	
	public String numberGenerate(Dto dto);
	
}
