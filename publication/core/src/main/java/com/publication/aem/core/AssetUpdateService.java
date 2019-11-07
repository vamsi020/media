package com.publication.aem.core;

import java.util.HashMap;
import java.util.Set;

public interface AssetUpdateService {

	void updateAssetProperties(Set<String> paths, HashMap<String, Object> metadata);

}
