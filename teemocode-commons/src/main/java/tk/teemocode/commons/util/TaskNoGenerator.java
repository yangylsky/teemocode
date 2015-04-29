package tk.teemocode.commons.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.exception.NoSuchObjectException;
import tk.teemocode.commons.exception.ProjectException;

@Transactional(rollbackFor = Throwable.class)
@Service
public class TaskNoGenerator {
	private static final Log log = LogFactory.getLog(TaskNoGenerator.class);

	private static String[] nos = new String[100];

	private static int noIdx = 0;

	public synchronized static String getNo(String taskType, String level) {
		taskType = taskType == null ? "" : taskType;
		level = level == null ? "" : level;
		String rNo = "";
		try {
			rNo = getpk(taskType, level, noIdx);
			for(int i = 0; i < 100; i++) {
				String no = nos[i];
				if(no != null && no.equals(rNo)) {
					try {
						Thread.sleep(1);
						rNo = getNo(taskType, level);
					} catch(InterruptedException e) {
						throw new ProjectException("生成任务流水号线程出错.", e);
					}
					break;
				}
			}
			nos[noIdx] = rNo;
			noIdx++;
			if(noIdx == 100) {
				noIdx = 0;
			}
		} catch(Throwable e) {
			log.error("生成任务批次流水号错误.", e);
			throw new NoSuchObjectException("生成任务批次流水号错误.");
		}
		return rNo;
	}

	private static String getpk(String taskType, String level, int idx) {
		String idxStr = idx < 10 ? "0" + idx : "" + idx;
		String docNo = DateUtil.getDateTime("yyyyMMddHHmmssS", new Date()).substring(2) + taskType + level + idxStr;
		return docNo;
	}
}
