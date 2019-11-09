package com.publication.aem.core.util;

public final class UserGroupsUtill {
	
	private UserGroupsUtill() {};
	
	public static final String getLocalGroupName (String groupName) {
		String localGroupName = null;
		if(groupName.contains(UserGroupsConsts.CREATOR_LOCAL_GROUP_SUFFIX)){
			localGroupName = groupName.replace(UserGroupsConsts.CREATOR_LOCAL_GROUP_SUFFIX, "");
		}else if (groupName.contains(UserGroupsConsts.MANAGER_LOCAL_GROUP_SUFFIX)){
			localGroupName = groupName.replace(UserGroupsConsts.MANAGER_LOCAL_GROUP_SUFFIX, "");
		}else if (groupName.contains(UserGroupsConsts.LEGAL_LOCAL_GROUP_SUFFIX)){
			localGroupName = groupName.replace(UserGroupsConsts.LEGAL_LOCAL_GROUP_SUFFIX, "");
		}else if (groupName.contains(UserGroupsConsts.LOCAL_GROUP_SUFFIX)){
			localGroupName = groupName.replace(UserGroupsConsts.LOCAL_GROUP_SUFFIX, "");
		}
		return localGroupName;
	}

}
