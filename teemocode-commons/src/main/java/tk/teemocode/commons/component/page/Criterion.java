package tk.teemocode.commons.component.page;

import java.io.Serializable;

import org.hibernate.Criteria;

public interface Criterion extends Serializable{
	public Criteria getCriteria();
	public String getHql();
}
