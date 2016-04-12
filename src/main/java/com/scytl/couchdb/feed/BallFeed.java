package com.scytl.couchdb.feed;

import java.util.concurrent.TimeUnit;

import org.ektorp.CouchDbConnector;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.impl.StdObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scytl.couchdb.model.Ball;

public class BallFeed implements Runnable{

	private final CouchDbConnector db;
	private ChangesCommand command;
	private final ObjectMapper objectMapper;
	
	public BallFeed(CouchDbConnector db, StdObjectMapperFactory objectMapperFactory) {
		super();
		this.db = db;
		this.objectMapper = objectMapperFactory.createObjectMapper();
	}

	public void run() {
		ChangesFeed feed = db.changesFeed(command);
		try{
			while(feed.isAlive()) {
				try {
					DocumentChange documentChange = feed.next(10, TimeUnit.SECONDS);
					if(documentChange != null) {
						String doc = documentChange.getDoc();
						Ball ball = objectMapper.readValue(doc, Ball.class);
						System.out.println(ball.getNumber());
					}
				} catch (InterruptedException e) {
					System.out.println("feed killed");
				}
			}
		} catch (Exception e){
			feed.cancel();
		}
		
	}

	public void setCommand(ChangesCommand command) {
		this.command = command;
	}
	
}
