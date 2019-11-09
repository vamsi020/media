package com.publication.aem.core.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;
import com.publication.aem.core.AssetUpdateService;

@Component(service=AssetUpdateService.class)
public class AssetUpdateServiceImpl implements AssetUpdateService{
	
	@Reference
	ResourceResolverFactory resolverFactory;
	
	private ResourceResolver resolver;
	private AssetManager assetManager;
	
	@Activate
	public void activate() throws LoginException{
		Map<String, Object> serviceMapper = new HashMap<>();
		serviceMapper.put(ResourceResolverFactory.SUBSERVICE, "contentUpdateSubservice");
		resolver = resolverFactory.getServiceResourceResolver(serviceMapper);
		assetManager = resolver.adaptTo(AssetManager.class);
	}
	
	@Override
	public void updateAssetProperties(Set<String> paths, HashMap<String, Object> metadata) {
		Iterator<String> pathItr = paths.iterator();
		while (pathItr.hasNext()) {
			String path = (String) pathItr.next();
			Asset asset = assetManager.getAsset(path);
			if(asset != null){
				Iterator<Entry<String, Object>> iterator= metadata.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					asset.getResourceMetadata().put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
