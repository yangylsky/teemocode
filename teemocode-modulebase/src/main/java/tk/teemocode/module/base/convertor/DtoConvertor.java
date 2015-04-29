package tk.teemocode.module.base.convertor;

import java.util.List;
import java.util.Set;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.dto.Dto;

public interface DtoConvertor<D extends Dto, B extends IBO> {
	public Class<D> getDtoClass();

	public D convert(B bo, String... fields);

	public List<D> convert(List<B> bos, String... fields);

	public Set<D> convert(Set<B> bos, String... fields);

	public Page<D> convert(Page<B> bos, String... fields);

	public B reverse(D dto, String... fields);

	public B reverse(B bo, D dto, String... fields);

	public List<B> reverse(List<D> dtos, String... fields);

	public Set<B> reverse(Set<D> dtos, String... fields);

	public Page<B> reverse(Page<D> dtos, String... fields);
}
