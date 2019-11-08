package com.publication.aem.core.workflow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.publication.aem.core.UserGroupService;
import com.publication.aem.core.util.UserGroupsConsts;
import com.publication.aem.core.util.UserGroupsUtill;

@Component(service=ParticipantStepChooser.class,
	immediate= true,
	property={"ParticipantStepChooser.SERVICE_PROPERTY_LABEL=Asset Final Approver Selection Process"})
public class AssetFinalApproverSelectionProcess implements ParticipantStepChooser{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	UserGroupService userGroupService;

	@Override
	public String getParticipant(WorkItem item, WorkflowSession wfSession, MetaDataMap metadata) throws WorkflowException {
		Workflow wf = item.getWorkflow();
		String wfInitiator = wf.getInitiator();
		String participant = wf.getInitiator();
		try {
			List<HistoryItem> assetApprovalItem = wfSession.getHistory(wf);
			Iterator<HistoryItem> listItr = assetApprovalItem.iterator();
			Authorizable initiatorUser = userGroupService.getAuthorizable(wfInitiator);
			Set<String> userGroups = userGroupService.getUserGroups(initiatorUser);
			String localGroupName = getLocalGroupName(userGroups);
			if(localGroupName != null ){
				participant = localGroupName+UserGroupsConsts.MANAGER_LOCAL_GROUP_SUFFIX;
				if(!userGroupService.isGroup(participant)) {
					participant = wfInitiator;
				}
				log.info("participant : "+participant);
			}
			while (listItr.hasNext()) {
				HistoryItem historyItem = (HistoryItem) listItr.next();
				if(historyItem.getWorkItem().getNode().getId().equals("node2")){
					participant = historyItem.getUserId();
				}
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return participant;
	}
	
	private String getLocalGroupName( Set<String> userGroups ){
		String localGroupName = null;
		Iterator<String> itr = userGroups.iterator();
		while (itr.hasNext()) {
			String groupName = itr.next();
			localGroupName = UserGroupsUtill.getLocalGroupName(groupName);
			if (localGroupName != null){
				break;
			}		
		}
		return localGroupName;
	}
}
