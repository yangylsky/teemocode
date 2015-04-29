package tk.teemocode.module.base.convertor;

import java.util.List;
import java.util.Set;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.dto.Dto;
import tk.teemocode.module.base.vo.Vo;

public interface VoConvertor<V extends Vo, D extends Dto> {
	public V convert(D dto, String... fields);

	public List<V> convert(List<D> dtos, String... fields);

	public Set<V> convert(Set<D> dtos, String... fields);

	public Page<V> convert(Page<D> dtos, String... fields);

	public D reverse(V vo, String... fields);

	public List<D> reverse(List<V> vos, String... fields);

	public Set<D> reverse(Set<V> vos, String... fields);

	public Page<D> reverse(Page<V> vos, String... fields);
}
