package com.publication.aem.core.workflow;

import java.util.Calendar;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service=WorkflowProcess.class,
		immediate=true,
		property={"process.label= Asset Absolute Time Update Process"})
public class AssetAbsoluteTimeUpdateProcess implements WorkflowProcess{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(WorkItem item, WorkflowSession wfSession, MetaDataMap metadata) throws WorkflowException {
		Workflow wf = item.getWorkflow();
		List<HistoryItem> history = wfSession.getHistory(wf);
		HistoryItem lastHistoryItem = history.get(history.size()-1);
		MetaDataMap lastItemMetadata = lastHistoryItem.getWorkItem().getMetaDataMap();
		String scheduleTime = lastItemMetadata.get("scheduleTime",String.class);
		if(scheduleTime != null && scheduleTime.equals("later")) {
			Calendar absoluteTime = lastItemMetadata.get("absoluteTime",Calendar.class);
			if(absoluteTime != null){
				MetaDataMap modifiedMap = wf.getMetaDataMap();
				modifiedMap.put("absoluteTime", Long.toString(absoluteTime.getTimeInMillis()));
				wf.getWorkflowData().getMetaDataMap().put("absoluteTime", Long.toString(absoluteTime.getTimeInMillis()));
				wfSession.updateWorkflowData(wf, wf.getWorkflowData());
			}
		}
	}
}