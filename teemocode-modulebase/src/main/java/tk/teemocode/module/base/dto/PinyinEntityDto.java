package tk.teemocode.module.base.dto;

public abstract class PinyinEntityDto extends EntityDto {
	private String pinyin;

	private String fullPinyin;

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}
}
