package com.scytl.couchdb.repository;

import com.scytl.couchdb.model.BingoCard;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.support.Views;

@Views ( value = {@View( name="nonAssigned", map = "function(doc) { if(!doc.userid) emit( null, doc._id )}"), 
		@View( name="assignedtoUser", map = "function(doc, req) { emit( doc.userid, doc._id )}")})
public class BingoCardRepository extends CouchDbRepositorySupport<BingoCard>{

	public BingoCardRepository(CouchDbConnector db, boolean createIfNotExists) {
		super(BingoCard.class, db, createIfNotExists);
		initStandardDesignDocument();
	}
}
