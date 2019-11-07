package com.publication.aem.core.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.publication.aem.core.UserGroupService;

@Component(service=UserGroupService.class)
public class UserGroupServiceImpl implements UserGroupService{
	
	@Reference
	ResourceResolverFactory resolverFactory;
	
	private ResourceResolver resolver;
	private UserManager userManager;
	
	@Activate
	public void activate() throws LoginException{
		Map<String, Object> serviceMapper = new HashMap<>();
		serviceMapper.put(ResourceResolverFactory.SUBSERVICE, "userGroupSubservice");
		resolver = resolverFactory.getServiceResourceResolver(serviceMapper);
		userManager = resolver.adaptTo(UserManager.class);
	}

	@Override
	public Authorizable getAuthorizable(String id) throws RepositoryException {
		return userManager.getAuthorizable(id);
	}

	@Override
	public Set<String> getUserGroups(Authorizable authorizable) throws RepositoryException {
		Iterator<Group> groupItr = authorizable.memberOf();
		Set<String> userGroups = new HashSet<>();
		while (groupItr.hasNext()) {
			Group group = (Group) groupItr.next();
			userGroups.add(group.getID());			
		}
		return userGroups;
	}
	@Override
	public boolean isGroup(String id) throws RepositoryException{
		return userManager.getAuthorizable(id).isGroup();
	}
	
	
	
	
	
	
	
	
	

}
