package tk.teemocode.module.base.bo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import tk.teemocode.commons.util.Pinyinable;

/**
 * 支持拼音检索的Entity BO
 * @author yangylsky
 *
 */
@MappedSuperclass
public abstract class PinyinEntityBO extends EntityBO implements Pinyinable {
	@Column(length = 50)
	private String pinyin;

	private String fullPinyin;

	@Override
	public String getPinyin() {
		return pinyin;
	}

	@Override
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	@Override
	public String getFullPinyin() {
		return fullPinyin;
	}

	@Override
	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	//==========================Domain Business Method==============================//

	@Override
	public String getPinyinableValue() {
		return StringUtils.isBlank(getName()) ? getDescription() : getName();
	}
}
