package com.teemocode.commons.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.teemocode.commons.exception.ProjectException;

public class NumberManageUtil {

	private static final Log log = LogFactory.getLog(NumberManageUtil.class);

	/**
	 * 单号前缀
	 */
	public static String TYPE_ACCOUNT = "1";

	public static String TYPE_DETAIL = "2";

	public static String TYPE_ORDER = "3";

	/**
	 * 账户单号后缀IDX
	 */
	private static int accountIdx = 0;

	/**
	 * 账户明细单号后缀IDX
	 */
	private static int detailIdx = 0;

	/**
	 * 订单号后缀IDX
	 */
	private static int orderIdx = 0;

	/**
	 * 账户号
	 * @param mNo
	 * @return
	 */
	public static String getAccountNo(String mNo) {
		String aNo = "";
		try {
			aNo+=TYPE_ACCOUNT;
			aNo+=mNo;
			aNo+=RandomUtil.getUniqueID();
			aNo+=DateUtil.getDateTime("yyyyMMddHHmmss", new Date()).substring(2);
			aNo+=getStringIdx(accountIdx);
			if (accountIdx>=100) {
				accountIdx=0;
			}else{
				accountIdx++;
			}
		} catch (Exception e) {
			log.error("生成账户号错误。",e);
    		throw new ProjectException("生成账户号错误。");
		}
		return aNo;
	}

	/**
	 * 账户明细号
	 * @param mNo
	 * @return
	 */
	public static String getAccountDetailNo(String mNo) {
		String detailNo = "";
		try {
			detailNo+=TYPE_DETAIL;
			detailNo+=mNo;
			detailNo+=RandomUtil.getUniqueID();
			detailNo+=DateUtil.getDateTime("yyyyMMddHHmmss", new Date()).substring(2);
			detailNo+=getStringIdx(detailIdx);
			if (detailIdx>=100) {
				detailIdx=0;
			}else{
				detailIdx++;
			}
		} catch (Exception e) {
			log.error("生成账户号错误。",e);
    		throw new ProjectException("生成账户号错误。");
		}
		return detailNo;
	}

	/**
	 * 订单号
	 * @param mNo 三位
	 * @return
	 */
	public static String getOrderNo(String mNo) {
		String detailNo = "";
		try {
			detailNo+=TYPE_ORDER;
			detailNo+=mNo;
			detailNo+=RandomUtil.getUniqueID();
			detailNo+=DateUtil.getDateTime("yyyyMMddHHmmss", new Date()).substring(2);
			detailNo+=getStringIdx(orderIdx);
			if (orderIdx>=100) {
				orderIdx=0;
			}else{
				orderIdx++;
			}
		} catch (Exception e) {
			log.error("生成账户号错误。",e);
    		throw new ProjectException("生成账户号错误。");
		}
		return detailNo;
	}

	private static String getStringIdx(int idx){
		String idxStr = idx < 10 ? "0" + idx :  "" + idx;
		return idxStr;
	}

}
