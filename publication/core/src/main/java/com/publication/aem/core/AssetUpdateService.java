package com.publication.aem.core;

import java.util.HashMap;
import java.util.Set;

import javax.jcr.RepositoryException;

public interface AssetUpdateService {

	public void updateAssetProperties(Set<String> paths, HashMap<String, Object> metadata);

}
