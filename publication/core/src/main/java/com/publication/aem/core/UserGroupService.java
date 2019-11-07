package com.publication.aem.core;

import java.util.Set;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Authorizable;

public interface UserGroupService {
	
	public Authorizable getAuthorizable(String id) throws RepositoryException;
	
	public Set<String> getUserGroups (Authorizable authorizable) throws RepositoryException;

	boolean isGroup(String id) throws RepositoryException;
		
}
