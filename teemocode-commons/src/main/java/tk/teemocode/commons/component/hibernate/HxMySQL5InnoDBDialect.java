package tk.teemocode.commons.component.hibernate;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class HxMySQL5InnoDBDialect extends MySQL5InnoDBDialect {
	public HxMySQL5InnoDBDialect() {
		super();
		registerFunction("find_in_set", new StandardSQLFunction("find_in_set", StandardBasicTypes.INTEGER));
	}
}
