package tk.teemocode.module.index.interceptor;

import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.index.convertor.ConvertorCache;
import tk.teemocode.module.index.convertor.IndexItemConvertor;
import tk.teemocode.module.search.Indexable;

@Component
@Aspect
public class SaveBoInterceptor extends IndexActionInterceptor {
	@SuppressWarnings({"unchecked", "rawtypes"})
//	@AfterReturning(value = "execution(* tk.teemocode.module.base.service.impl.LocalServiceImpl.saveOrUpdate(..))")
	public void after(JoinPoint jp) {
		String className = jp.getThis().toString();
		String methodName = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		logger.debug(className + "invoke " + methodName + "end...");
		IBO bo = (IBO) args[0];
		Set<IndexItemConvertor> convertors = ConvertorCache.getConvertor(bo.getClass());
		for(IndexItemConvertor convertor : convertors) {
			Indexable intexItem = convertor.convert(bo);
			getCreateIndexItemProcessor().send(intexItem);
		}
	}
}
