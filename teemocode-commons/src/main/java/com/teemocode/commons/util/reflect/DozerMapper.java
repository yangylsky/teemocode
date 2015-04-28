package com.teemocode.commons.util.reflect;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * 辅助DTO复制的Dozer工具类的单例wrapper.
 *
 * Dozer在同一JVM里使用单例即可,无需重复创建.
 * 但Dozer4自带的DozerBeanMapperSingletonWrapper必须使用dozerBeanMapping.xml作参数初始化,因此重新实现无配置文件的版本.
 *
 */
public final class DozerMapper {
	private static Mapper instance = new DozerBeanMapper();//使用预初始化避免并发问题.

	private DozerMapper() {
	}

	public static Mapper getInstance() {
		return instance;
	}
}
