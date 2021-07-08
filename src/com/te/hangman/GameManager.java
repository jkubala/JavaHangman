package com.te.hangman;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameManager {
	Scanner scanner = new Scanner(System.in);
	int nOfTriesLeft = 15;
	boolean exitGame = false;
	enum GameState {
		MAIN_MENU,
		GAME_LOOP
	}
	GameState gameState = GameState.MAIN_MENU;
	List<Player> players = new ArrayList<Player>();
	
	public void newGame() {
		do {
			switch (gameState) {
			case MAIN_MENU: {
				handleMainMenu();
				break;
			}
			default:
				break;
			}
		}
		while(!exitGame);
	}
	
	public void handleMainMenu() {
		if(players.size() < 2) {
			System.out.println("1. Play (Unavailable - Must be at least 2 players)");
		}
		else {
			System.out.println("1. Play");
		}
		System.out.println("2. Create new player\n"
				+ "3. Delete player\n"
				+ "4. Exit");
		int userinput = 0;
		try {
			userinput = scanner.nextInt();
		} 
		catch (InputMismatchException e) {
			scanner.nextLine();
		}
		
		switch (userinput) {
		case 1: {
			if(players.size() >= 2) {
				gameState = GameState.GAME_LOOP;
			}
		}
		case 2: {
			System.out.println("Creating new player");
			break;
		}
		case 3: {
			System.out.println("Deleting player");
			break;
		}
		case 4: {
			exitGame = true;
			break;
		}
		default:
			System.out.println("Invalid choice");
		}
	}
}
