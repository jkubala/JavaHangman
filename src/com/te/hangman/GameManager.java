package com.te.hangman;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * GameManager class is responsible for main menu and coordination of other
 * components - flow of the game
 */
public class GameManager {
	private enum GameState {
		MAIN_MENU, GAME_LOOP, END_BOARD
	}

	private Scanner scanner = new Scanner(System.in);
	private PlayerManager playerManager = new PlayerManager();
	private WordManager wordManager = new WordManager();
	private GameState gameState = GameState.MAIN_MENU;
	private boolean exitGame = false;

	/**
	 * Creates a new game instance, main body of the app
	 */
	void newGame() {
		do {
			switch (gameState) {
			case MAIN_MENU: {
				handleMainMenu();
				break;
			}
			case GAME_LOOP: {
				handleGameLogic();
				break;
			}
			case END_BOARD: {
				displayEndBoard();
				break;
			}
			default:
				break;
			}
		} while (!exitGame);
	}

	/**
	 * Main menu processes userInput and does operations with player accounts and
	 * checks if there are enough players for entering the game
	 */
	private void handleMainMenu() {
		printMainMenu();

		int userinput = 0;
		try {
			userinput = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		switch (userinput) {
		case 1: {
			if (playerManager.nOfPlayers() >= 2) {
				gameState = GameState.GAME_LOOP;
			}
			break;
		}
		case 2: {
			playerManager.createPlayer(scanner);
			break;
		}
		case 3: {
			playerManager.deletePlayer(scanner);
			break;
		}
		case 4: {
			playerManager.showCurrentPlayers(scanner);
			break;
		}
		case 5: {
			exitGame = true;
			break;
		}
		default:
			System.out.println("Invalid choice");
			scanner.nextLine();
			break;
		}
	}

	/**
	 * Handles graphic part of main menus
	 */
	private void printMainMenu() {
		Utility.clearScreen();
		if (playerManager.nOfPlayers() < 2) {
			System.out.println("1. Play (Unavailable - Must be at least 2 players)");
		} else {
			System.out.println("1. Play");
		}
		System.out.println("2. Create new player\n" + "3. Delete player\n" + "4. Show existing players\n" + "5. Exit");
	}

	/**
	 * Sets gamemaster, wordToGuess and processes wordGuessing, while changing
	 * player turns. When handleGuessing returns true - word is guessed, it changes
	 * state to end board, which ends the game round.
	 */
	private void handleGameLogic() {
		if (!wordManager.hasValidWordToGuess()) {
			playerManager.setNewGameMaster();
			wordManager.setWordToGuess(scanner, playerManager.getCurrentGameMaster());
			wordManager.setDisplayedWordToGuess();
		}
		playerManager.setNewPlayerTurn();
		if (wordManager.handleGuessing(scanner, playerManager.getCurrentPlayer(),
				playerManager.getCurrentGameMaster())) {
			gameState = GameState.END_BOARD;
		} else if (wordManager.exitRound()) {
			wordManager.resetGameVariables();
			playerManager.resetScoreOfAllPlayersInThisRound();
			gameState = GameState.MAIN_MENU;
		}
	}

	/**
	 * End game main menu. Shows player scores and either plays another round, goes
	 * to main menu, or exits the application
	 */
	private void displayEndBoard() {
		System.out.println("1. Play again\n2. Main Menu\n3. Exit");
		playerManager.printPlayerScores(wordManager.wordWasGuessed());
		int userinput = 0;
		try {
			userinput = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		switch (userinput) {
		case 1: {
			wordManager.resetGameVariables();
			gameState = GameState.GAME_LOOP;
			break;
		}
		case 2: {
			wordManager.resetGameVariables();
			gameState = GameState.MAIN_MENU;
			break;
		}
		case 3: {
			exitGame = true;
			break;
		}
		default:
			System.out.println("Invalid choice");
			scanner.nextLine();
			break;
		}
	}
}
