package tk.teemocode.commons.util.bean;

public class ClassField {
	private final String name;

	private final String declaringClass;

	public ClassField(String definedIn, String name) {
		this.name = name;
		this.declaringClass = definedIn;
	}

	public ClassField(Class<?> definedIn, String name) {
		this(definedIn == null ? null : definedIn.getName(), name);
	}

	public String getName() {
		return this.name;
	}

	public String getDeclaringClass() {
		return this.declaringClass;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(obj instanceof ClassField) {
			final ClassField field = (ClassField) obj;
			if((declaringClass == null && field.declaringClass != null)
					|| (declaringClass != null && field.declaringClass == null)) {
				return false;
			}
			return name.equals(field.getName()) && (declaringClass == null || declaringClass.equals(field.getDeclaringClass()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode() ^ (declaringClass == null ? 0 : declaringClass.hashCode());
	}

	@Override
	public String toString() {
		return (declaringClass == null ? "" : declaringClass + ".") + name;
	}
}
