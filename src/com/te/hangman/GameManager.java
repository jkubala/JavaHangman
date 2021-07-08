package com.te.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameManager {
	Scanner scanner = new Scanner(System.in);
	int defaultNOfTriesLeft = 15;
	int nOfTriesLeft = defaultNOfTriesLeft;
	String wordToGuess = "";
	String wordToGuessUnderscore = "";
	boolean gameWon = false;
	boolean exitGame = false;

	enum GameState {
		MAIN_MENU, GAME_LOOP, END_BOARD
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

	private void displayEndBoard() {
		System.out.println("1. Play again\n2. Exit");
		int userinput = 0;
		try {
			userinput = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			scanner.nextLine();
		}

		switch (userinput) {
		case 1: {
			resetGameVariables();
			gameState = GameState.GAME_LOOP;
			break;
		}
		case 2: {
			exitGame = true;
			break;
		}
		default:
			System.out.println("Invalid choice");
			scanner.nextLine();
			break;
		}
	}

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
			if (players.size() >= 2) {
				gameState = GameState.GAME_LOOP;
			}
			break;
		}
		case 2: {
			createPlayer();
			break;
		}
		case 3: {
			deletePlayer();
			break;
		}
		case 4: {
			showCurrentPlayers();
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

	private void handleGameLogic() {
		if (wordToGuess.trim().isEmpty()) {
			setWordToGuess();
			wordToGuess = wordToGuess.toUpperCase();
			setDisplayedWordToGuess();
		}
		printGameScreen();
		handleGuessing();
	}

	private void setDisplayedWordToGuess() {
		for (int i = 0; i < wordToGuess.length(); i++) {
			wordToGuessUnderscore += "_ ";
		}
	}

	private void handleGuessing() {
		System.out.println("Enter a letter, or whole word");
		String guess = scanner.nextLine();
		guess = guess.toUpperCase();
		if (guess.length() == 0) {
			System.out.println("Error - empty guess inputed!");
		} else if (guess.length() == 1) {
			if (wordToGuess.indexOf(guess.charAt(0)) != -1) {
				updateDisplayedSecretWord(guess.charAt(0));
				if (wordToGuessUnderscore.indexOf('_') == -1) {
					gameState = GameState.END_BOARD;
				}
			} else {
				nOfTriesLeft--;
			}
		} else {
			if (wordToGuess.equals(guess)) {
				gameState = GameState.END_BOARD;
			} else {
				nOfTriesLeft--;
			}
		}
	}

	private void setWordToGuess() {
		boolean validGuessWord = false;
		do {
			clearScreen();
			System.out.println("Enter word, that will be guessed:");
			wordToGuess = scanner.nextLine();
			if (!wordToGuess.trim().isEmpty() && wordToGuess.length() > 1 && wordToGuess.matches("[a-zA-Z]+")) {
				validGuessWord = true;
			} else {
				System.out.println("Invalid word entered!");
				scanner.nextLine();
			}
		} while (!validGuessWord);
		clearScreen();
		System.out.println("You chose:\n" + wordToGuess + "\nEnter \"Y\" to change it, or press Enter to continue");
		if (scanner.nextLine().toLowerCase().equals("y")) {
			wordToGuess = "";
			setWordToGuess();
		}
	}

	private void printGameScreen() {
		clearScreen();
		System.out.println("Tries left: " + nOfTriesLeft + "\nSecret word:\n" + wordToGuessUnderscore);
	}

	private void showCurrentPlayers() {
		clearScreen();
		if (players.size() > 0) {
			System.out.println("Current players:\n" + Arrays.toString(players.toArray()));
		} else {
			System.out.println("There have not been any players created yet");
		}
		scanner.nextLine();
	}

	private void printMainMenu() {
		clearScreen();
		if (players.size() < 2) {
			System.out.println("1. Play (Unavailable - Must be at least 2 players)");
		} else {
			System.out.println("1. Play");
		}
		System.out.println("2. Create new player\n" + "3. Delete player\n" + "4. Show current players\n" + "5. Exit");
	}

	private void createPlayer() {
		boolean validName = false;
		String newPlayerName;
		clearScreen();
		do {

			System.out.println("Enter player name:");
			newPlayerName = scanner.nextLine();
			if (newPlayerName.trim().isEmpty()) {
				System.out.println("Invalid name!");
				scanner.nextLine();
			} else if (playerExists(newPlayerName)) {
				System.out.println("Player already exists!");
				scanner.nextLine();
			} else {
				validName = true;
			}
		} while (!validName);
		Player newPlayer = new Player(newPlayerName);
		players.add(newPlayer);
	}

	private void deletePlayer() {
		if (players.size() == 0) {
			System.out.println("There are no player that could be deleted!");
			scanner.nextLine();
			return;
		}
		String playerToDelete;
		System.out.println("Enter player name:");
		playerToDelete = scanner.nextLine();
		if (playerExists(playerToDelete)) {
			deletePlayerByID(playerToDelete);
		}
	}

	private void updateDisplayedSecretWord(char letterToUpdateWith) {
		char[] DispSecWordArr = wordToGuessUnderscore.toCharArray();
		for (int i = 0; i < wordToGuess.length(); i++) {
			if (wordToGuess.charAt(i) == letterToUpdateWith) {
				DispSecWordArr[i * 2] = letterToUpdateWith;
			}
		}
		wordToGuessUnderscore = String.valueOf(DispSecWordArr);
	}

	private void clearScreen() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}

	private boolean playerExists(final String name) {
		return players.stream().filter(player -> player.getName().equals(name)).findFirst().isPresent();
	}

	private void deletePlayerByID(final String name) {
		players.removeIf(player -> player.getName().equals(name));
	}
	
	private void resetGameVariables()
	{
		gameWon = false;
		nOfTriesLeft = defaultNOfTriesLeft;
		wordToGuess = "";
		wordToGuessUnderscore = "";
	}
}
