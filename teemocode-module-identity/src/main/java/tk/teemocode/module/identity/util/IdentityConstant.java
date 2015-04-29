package tk.teemocode.module.identity.util;

import tk.teemocode.module.util.SystemConstant;

public class IdentityConstant extends SystemConstant {
	public static final String Admin_Username = "admin";

	public static final String User_Origin_Password = "123456";

	/**
	 * 系统管理员
	 */
	public static final int User_Type_Sys = 1;

	/**
	 * 机构管理员
	 */
	public static final int User_Type_GroupSys = 2;

	/**
	 * 普通用户
	 */
	public static final int User_Type_Normal = 3;

	/**
	 * 权限类型，url权限类型
	 */
	public static final Integer Auth_Type_Url = 10;

	/**
	 * 权限类型，方法权限类型
	 */
	public static final Integer Auth_Type_Method = 20;

	/**
	 * 权限类型，数据权限类型
	 */
	public static final Integer Auth_Type_Data = 30;

	/**
	 * 权限类型，功能权限类型
	 */
	public static final Integer Auth_Type_Module = 40;

	/**
	 * 权限类型，功能操作类型
	 */
	public static final Integer Auth_Type_Operate = 50;

	/**
	 * 角色类型，系统角色 值：10
	 */
	public static final Integer Role_Type_Sys = 10;

	/**
	 * 角色类型，机构角色 值：20
	 */
	public static final Integer Role_Type_Group = 20;
}
