package com.teemocode.commons.component.export.excel;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ImportDataResult implements Serializable {
	private static final long serialVersionUID = -6241448372465037950L;

	private boolean success = true;

	private boolean overviewAllError = false;

	private int validNum = 0;

	private int existNum = 0;

	private int ignoreNum;

	private int newNum = 0;

	private int updateNum = 0;

	private int errorNum = 0;

	private StringBuilder warnMsg = new StringBuilder();

	private StringBuilder errorMsg = new StringBuilder();

	private StringBuilder infoMsg = new StringBuilder();

	public ImportDataResult(boolean overviewAllError) {
		this.overviewAllError = overviewAllError;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isOverviewAllError() {
		return overviewAllError;
	}

	public int getValidNum() {
		return validNum;
	}

	public int getExistNum() {
		return existNum;
	}

	public int getIgnoreNum() {
		return ignoreNum;
	}

	public int getNewNum() {
		return newNum;
	}

	public int getUpdateNum() {
		return updateNum;
	}

	public void addValidNum() {
		validNum++;
	}

	public void addExistNum() {
		existNum++;
	}

	public void addIgnoreNum() {
		ignoreNum++;
	}

	public void addNewNum() {
		newNum++;
	}

	public void addNewNum(int num) {
		newNum += num;
	}

	public void clearNewNum() {
		newNum = 0;
	}

	public void addUpdateNum() {
		updateNum++;
	}

	public void clearUpdateNum() {
		updateNum = 0;
	}

	public int getErrorNum() {
		return errorNum;
	}

	public void addErrorNum() {
		errorNum++;
	}

	public String getErrorMsg() {
		return errorMsg.toString();
	}

	public void addWarnMsg(String msg) {
		if(StringUtils.isNotBlank(msg)) {
			warnMsg.append("\\n\\t").append(msg);
		}
	}

	public void addErrorMsg(String msg) {
		if(StringUtils.isNotBlank(msg)) {
			errorMsg.append("\\n\\t").append(msg);
		}
	}

	public void addErrorMsg2(String msg) {
		if(StringUtils.isNotBlank(msg)) {
			addErrorNum();
			errorMsg.append("\\n\\t").append(msg);
		}
	}

	public void addInfoMsg(String msg) {
		if(StringUtils.isNotBlank(msg)) {
			infoMsg.append("\\n\\t").append(msg);
		}
	}

	@Override
	public String toString() {
		return "ImportDataResult(success:" + success + ","
		+ "validNum:" + validNum + ","
		+ "existNum:" + existNum + ","
		+ "ignoreNum:" + ignoreNum + ","
		+ "newNum:" + newNum + ","
		+ "updateNum:" + updateNum + ")";
	}

	public String getImportLog() {
		return "结果: " + (success ? "成功" : "失败")
		+ "\\r\\n有效数据条数: " + validNum
		+ "\\r\\n已存在数据条数: " + existNum
		+ "\\r\\n忽略数据条数: " + ignoreNum
		+ "\\r\\n新导入数据条数: " + newNum
		+ "\\r\\n更新数据条数: " + updateNum
		+ "\\r\\n错误数据条数: " + errorNum
		+ "\\r\\n警告信息:" + warnMsg
		+ "\\r\\n错误信息:" + errorMsg
		+ "\\r\\n信息:" + infoMsg;
	}
}
