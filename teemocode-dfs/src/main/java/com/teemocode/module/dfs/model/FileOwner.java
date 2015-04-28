package com.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.Owner;

public class FileOwner extends Owner {
	public FileOwner() {
		super();
	}

	public FileOwner(String userId) {
		this(userId, null);
	}

	public FileOwner(String userId, String userDisplayName) {
		super(userId, userDisplayName);
	}

	public FileOwner(Owner owner) {
		this(owner.getId(), owner.getDisplayName());
	}
}
