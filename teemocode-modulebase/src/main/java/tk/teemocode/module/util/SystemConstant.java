package tk.teemocode.module.util;

public class SystemConstant {
	/**
	 * 当前有效的
	 */
	public static final int Tag_Active = 10;

	/**
	 * 当前有效且激活(上架)的
	 */
	public static final int Tag_PutOn = 40;

	/**
	 * 当前失效(下架)的
	 */
	public static final int Tag_PullOff = 50;

	/**
	 * 已归档的
	 */
	public static final int Tag_Archived = 60;

	/**
	 * 已禁止的
	 */
	public static final int Tag_Disabled = 70;

	/**
	 * 已标记为删除的
	 */
	public static final int Tag_Deleted = 99;

	/**
	 * 默认排序号
	 */
	public static final int SortIdx_Default = 1000;

	/**
	 * 身份证
	 */
	public static final int Identity_Type_IDCard = 1;

	/**
	 * 护照
	 */
	public static final int Identity_Type_Passport = 2;

	public static final String Disruptor_Topic_CreateItem = "createIndexItem";

	public static final String Disruptor_Topic_DeleteItem = "deleteIndexItem";
}
