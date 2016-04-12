package com.scytl.couchdb.repository;

import com.scytl.couchdb.model.User;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class UserRepository extends CouchDbRepositorySupport<User>{

	public UserRepository(CouchDbConnector db, boolean createIfNotExists) {
		super(User.class, db, createIfNotExists);
		initStandardDesignDocument();
	}
}
