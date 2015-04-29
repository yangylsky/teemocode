package tk.teemocode.web.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tk.teemocode.commons.exception.ProjectException;
import tk.teemocode.module.dfs.model.FileSummary;
import tk.teemocode.web.action.SimpleActionSupport;

/**
 * 文件上传action
 *
 * @author yangylsky
 */
@Controller
@RequestMapping("/upload/")
public class FileUploadAction extends SimpleActionSupport {
	/**
	 *
	 * @param request
	 * @param filedata
	 * @param resBase - 图片的路径base，和imgUrlName结合使用
	 * @param imgUrlName - 返回的imgUrl的json name，不指定的话则为{props:{fileName: xxx}}的格式
	 * @param map
	 * @return
	 */
	@RequestMapping("upload")
	public String uploadImage(HttpServletRequest request, @RequestParam("filedata") MultipartFile filedata, String resBase,
			String imgUrlName, ModelMap map) {
		try {
			//FIXME -
			String userId = null;
			FileSummary fileSummary = FileUploadUtil.uploadImageFile(filedata, userId);

			Map<String, Object> result = new HashMap<>();
			if(StringUtils.isNotBlank(resBase) && StringUtils.isNotBlank(imgUrlName)) {
				result.put(imgUrlName, resBase + fileSummary.getKey());
			}
			Map<String, String> props = new HashMap<>();
			props.put("fileName", fileSummary.getKey());
			result.put("props", props);
			result.put("success", true);
			return renderJsonObj(result, map);
		} catch(ProjectException pe) {
			logger.error("上传失败！", pe);
			return renderJsonError(pe.getMessage(), map);
		} catch(Exception e) {
			logger.error("上传失败！", e);
			return renderJsonError("上传失败！" + e.getMessage(), map);
		}
	}
}
