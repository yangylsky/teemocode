package com.teemocode.commons.util.bean;

public enum NullValue {
	NullByte(Byte.valueOf(Byte.MIN_VALUE)),
	Nullbyte(Byte.MIN_VALUE),
	NullInteger(Integer.valueOf(Integer.MIN_VALUE)),
	Nullint(Integer.MIN_VALUE),
	NullLong(Long.valueOf(Long.MIN_VALUE)),
	Nulllong(Long.MIN_VALUE),
	NullFloat(Float.valueOf(Float.MIN_VALUE)),
	Nullfloat(Float.MIN_VALUE),
	NullDouble(Double.valueOf(Double.MIN_VALUE)),
	Nulldouble(Double.MIN_VALUE),
	NullString("Null[String]"),
	NullObject(new Object());

	public final Object value;

	NullValue(Object value) {
		this.value = value;
	}

	public Object value() {
		return value;
	}

	public static NullValue byValue(String value) {
		NullValue[] values = NullValue.values();
		for(NullValue v : values) {
			if(v.value.equals(value)) {
				return v;
			}
		}
		throw new IllegalArgumentException("No enum of value: " + value);
	}
}
