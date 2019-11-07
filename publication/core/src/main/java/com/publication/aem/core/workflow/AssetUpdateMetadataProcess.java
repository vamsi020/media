package com.publication.aem.core.workflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.path.Path;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.publication.aem.core.AssetUpdateService;
import com.publication.aem.core.UserGroupService;

@Component(service=WorkflowProcess.class,
	immediate= true,
	property={"process.label= Asset Absolute Time Update Process"})
public class AssetUpdateMetadataProcess implements WorkflowProcess{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	UserGroupService userGroupService;
	
	@Reference
	AssetUpdateService assetUpdateService;

	@Override
	public void execute(WorkItem item, WorkflowSession wfSession, MetaDataMap metadata) throws WorkflowException {
		Workflow wf = item.getWorkflow();
		List<HistoryItem> history = wfSession.getHistory(wf);
		HistoryItem lastHistoryItem = history.get(history.size()-1);
		MetaDataMap lastItemMetadata = lastHistoryItem.getWorkItem().getMetaDataMap();
		ResourceResolver resolver = wfSession.adaptTo(ResourceResolver.class);
		updateAsset(wf, lastItemMetadata, resolver);
	}
	
	private void updateAsset(Workflow wf, MetaDataMap metadata, ResourceResolver resolver) {
		Path payload = (Path) wf.getWorkflowData().getPayload();
		String payloadPath = payload.getPath();
		String accessType = metadata.get("accessType", String.class);
		String[] customerTags = metadata.get("customerTags", String[].class);
		HashMap<String, Object> map = new HashMap<>();
		map.put("accessType", accessType);
		map.put("customerTags", customerTags);
		Set<String> paths = new HashSet<>();
		if(payloadPath.startsWith("/dam")){
			log.info("Path in dam ");
			paths.add(payloadPath);
			assetUpdateService.updateAssetProperties(paths, map);				
		}else if(payloadPath.startsWith("/etc")){
			log.info("Path in etc muti sellect ");
			Resource packageResource = resolver.getResource(payloadPath);
			if(packageResource != null){
				Resource filterRes = packageResource.getChild("filter");
				if(filterRes != null && filterRes.isResourceType("cq/workflow/components/collection/definition/resourcelist")){
					Iterator<Resource> resItr = filterRes.getChildren().iterator();
					while (resItr.hasNext()) {
						Resource resource = (Resource) resItr.next();
						String path = resource.getValueMap().get("root", String.class);
						if (path != null){
							paths.add(path);
						}
					}
					assetUpdateService.updateAssetProperties(paths, map);							
				}
			}
		}
	}
}
