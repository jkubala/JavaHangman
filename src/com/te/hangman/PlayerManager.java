package com.te.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PlayerManager {
	private List<Player> players = new ArrayList<Player>();
	private Player currentPlayer = null;
	private Player currentGameMaster = null;

	Player getCurrentPlayer() {
		return currentPlayer;
	}

	Player getCurrentGameMaster() {
		return currentGameMaster;
	}

	List<Player> getPlayers() {
		return players;
	}

	void createPlayer(Scanner scanner) {
		boolean validName = false;
		String newPlayerName;
		do {
			Utility.clearScreen();
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

	void deletePlayer(Scanner scanner) {
		Utility.clearScreen();
		if (players.size() == 0) {
			System.out.println("There are no players that could be deleted!");
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

	Player getNextPlayerFrom(Player playerFrom) {
		int indexFrom = players.indexOf(playerFrom);
		if (indexFrom + 1 < players.size()) {
			return players.get(indexFrom + 1);
		} else {
			return players.get(0);
		}
	}

	Player getPlayerAtIndex(int indexAt) {
		return players.get(indexAt);
	}

	int nOfPlayers() {
		return players.size();
	}

	boolean playerExists(final String name) {
		return players.stream().filter(player -> player.getName().equals(name)).findFirst().isPresent();
	}

	void deletePlayerByID(final String name) {
		players.removeIf(player -> player.getName().equals(name));
	}

	void setNewPlayerTurn() {
		if (currentPlayer == null) {
			currentPlayer = getPlayerAtIndex(0);
		} else {
			currentPlayer = getNextPlayerFrom(currentPlayer);
		}

		// Shift one more position from where it currently is (on gameMaster)
		if (currentPlayer.getName().equals(currentGameMaster.getName())) {
			currentPlayer = getNextPlayerFrom(currentPlayer);
		}
	}

	void setNewGameMaster() {
		if (currentGameMaster == null) {
			currentGameMaster = getPlayerAtIndex(0);
		} else {
			currentGameMaster = getNextPlayerFrom(currentGameMaster);
		}
	}

	void showCurrentPlayers(Scanner scanner) {
		Utility.clearScreen();
		if (nOfPlayers() > 0) {
			System.out.println("Current players:\n" + Arrays.toString(players.toArray()));
		} else {
			System.out.println("There have not been any players created yet");
		}
		scanner.nextLine();
	}

	void printPlayerScores(boolean guessingTeamWon) {
		if (guessingTeamWon) {
			System.out.println("The guessing team won!");
			setScoreOfGuessersDuringThisRound();
		} else {
			System.out.println("The gamemaster won!");
			currentGameMaster.setScore(currentGameMaster.getScoreToAddThisRound());
		}
		List<Player> scoreboardToSortList = new ArrayList<Player>(getPlayers());
		scoreboardToSortList.sort(Comparator.comparing(Player::getScore));
		Collections.reverse(scoreboardToSortList);

		for (int i = 0; i < scoreboardToSortList.size(); i++) {
			if (getCurrentGameMaster().getName().equals(scoreboardToSortList.get(i).getName())) {
				System.out.println(i + 1 + getPlaceSuffix(i + 1) + scoreboardToSortList.get(i).getName() + " - Score - "
						+ scoreboardToSortList.get(i).getScore() + " - gamemaster");
			} else {
				System.out.println(i + 1 + getPlaceSuffix(i + 1) + scoreboardToSortList.get(i).getName() + " - Score - "
						+ scoreboardToSortList.get(i).getScore());
			}
		}
	}

	private void setScoreOfGuessersDuringThisRound() {
		for (Player player : players) {
			if (!player.equals(currentGameMaster)) {
				player.setScore(player.getScoreToAddThisRound());
			}
		}
	}

	private String getPlaceSuffix(int place) {
		switch (place) {
		case 1: {
			return "st - ";
		}
		case 2: {
			return "nd - ";
		}
		case 3: {
			return "rd - ";
		}
		default:
			return "th - ";
		}
	}
}
