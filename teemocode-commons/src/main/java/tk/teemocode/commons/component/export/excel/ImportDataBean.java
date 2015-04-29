package tk.teemocode.commons.component.export.excel;

import java.io.InvalidObjectException;
import java.io.Serializable;

import tk.teemocode.commons.util.lang.StringUtil;

public abstract class ImportDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 验证数据是否有效
	 * @param rowNum 导入数据所处行号
	 * @return 校验通过返回""，校验失败返回全部错误信息
	 * @throws InvalidObjectException
	 */
	public void validate(int rowNum, ImportDataResult importResult) throws InvalidObjectException {
		boolean isError = false;
		for(ValidateItem item : getValidateItems()) {
			if(item.isError()) {
				importResult.addErrorMsg(rowNumMsg(rowNum) + item.getErrorMsg());
				isError = true;
			}
		}
		if(isError) {
			importResult.addErrorNum();
			if(importResult.isOverviewAllError()) {
				throw new InvalidObjectException("无效的数据  - 行[" + rowNum + "].");
			} else {
				throw new InvalidObjectException("无效的数据  - " + importResult.getErrorMsg());
			}
		} else {
			autoCompleteField();
		}
	}

	/**
	 * 当数据有效时，根据已有字段自动设置一些其他字段的值
	 */
	protected void autoCompleteField() {
	}

	/**
	 * 要验证有效性的条目
	 * @return
	 */
	protected abstract ValidateItem[] getValidateItems();

	public static String rowNumMsg(int rowNum) {
		return "[第" + rowNum + "行]:";
	}

	protected String fixNo(String no) {
		return StringUtil.fixNo(no);
	}

	protected String fixValue(String value) {
		return StringUtil.fixValue(value);
	}
}