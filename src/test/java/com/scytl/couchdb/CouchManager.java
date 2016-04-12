package com.scytl.couchdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.ektorp.UpdateConflictException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.scytl.couchdb.model.Ball;
import com.scytl.couchdb.model.BingoCard;
import com.scytl.couchdb.repository.BallRepository;
import com.scytl.couchdb.repository.BingoCardRepository;
import com.scytl.couchdb.spring.CouchDbCrudContext;


public class CouchManager {
public static ApplicationContext CONTEXT;
	
	private LinkedBlockingQueue<Integer> numbers;

	@BeforeClass
	public static void setup(){
		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.registerShutdownHook();
		context.register(CouchDbCrudContext.class);
		context.refresh();
		CONTEXT=context;
	}

	@Test
	public void insertCards() throws IOException, InterruptedException{
		BingoCardRepository bingoCardRepository = CONTEXT.getBean(BingoCardRepository.class);
		createCards(bingoCardRepository);
		createInitialNumbers();
		insertBalls();
	}

	private void createInitialNumbers() {
		List<Integer> numbers = new ArrayList<Integer>(99);
		for (int i = 1; i <= 90; i++) {
			numbers.add(Integer.valueOf(i));
		}
		Collections.shuffle(numbers);
		this.numbers = new LinkedBlockingQueue<Integer>(numbers);
	}

	private void insertBalls() throws IOException, InterruptedException {
		BallRepository ballRepository = CONTEXT.getBean(BallRepository.class);
		List<Ball> all = ballRepository.getAll();
		for (Ball ball : all) {
			ballRepository.remove(ball);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean read = true;
		while (read){
	        System.out.print("Waiting until generation (press ANY key to generate, or write exit to stop)...");
	        String s = br.readLine();
	        if(s.toLowerCase().equals("exit")){
	        	read = false;
	        } else {
	        	Integer number = numbers.take();
	        	Ball entity = new Ball();
	        	entity.setNumber(number);
	        	entity.setGame("bingo");
	        	System.out.println("Ball wih number " + number + " created");
				ballRepository.add(entity);
	        }
		}
	}

	private void createCards(BingoCardRepository bingoCardRepository) {
		List<BingoCard> cards = bingoCardRepository.getAll();
		for (BingoCard card : cards) {
			bingoCardRepository.remove(card);
		}
		for(int i = 1; i<= 60 ; i++){
			BingoCard entity = new BingoCard();
			entity.setId(Integer.toString(i));
			try {
				bingoCardRepository.add(entity);
			} catch (UpdateConflictException e){
				
			}
		}
		
	}
}
