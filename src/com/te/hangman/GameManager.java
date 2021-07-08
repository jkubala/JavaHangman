package com.te.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameManager {
	Scanner scanner = new Scanner(System.in);
	int nOfTriesLeft = 15;
	boolean exitGame = false;

	enum GameState {
		MAIN_MENU, GAME_LOOP
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
			}
			default:
				break;
			}
		} while (!exitGame);
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
		}
	}
	

	private void handleGameLogic() {
		System.out.println("Playing game");
	}

	private void showCurrentPlayers() {
		clearScreen();
		if (players.size() > 0) {
			System.out.println("Current players:\n" + Arrays.toString(players.toArray()));
		}
		else {
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
		String playerToDelete;
		System.out.println("Enter player name:");
		playerToDelete = scanner.nextLine();
		if (playerExists(playerToDelete)) {
			System.out.println("WEEEE");
			deletePlayerByID(playerToDelete);
		}

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
}
