package com.scytl.couchdb.repository;

import com.scytl.couchdb.model.Ball;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.Filters;

@Filters( value ={@Filter( name = "codeEveningGame", function = "function(doc, req) { if(doc.game == req.query.game) { return true; } return false; }")})
public class BallRepository extends CouchDbRepositorySupport<Ball>{

	public BallRepository(CouchDbConnector db, boolean createIfNotExists) {
		super(Ball.class, db, createIfNotExists);
		initStandardDesignDocument();
	}
}
