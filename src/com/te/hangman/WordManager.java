package com.te.hangman;

import java.util.Scanner;

/**
 * Handles the game main loop - setting and guessing the word
 */
public class WordManager {
	private String wordToGuess = "";
	private String wordToGuessUnderscore = "";
	private String guessedLetters = "";
	private int defaultNOfTriesLeft = 15;
	private int nOfTriesLeft = defaultNOfTriesLeft;
	private boolean exitRound = false;

	public boolean exitRound() {
		return exitRound;
	}

	String getWordToGuess() {
		return wordToGuess;
	}

	String getWordToGuessUnderscore() {
		return wordToGuessUnderscore;
	}

	boolean wasGuessed(String guess) {
		return guessedLetters.contains(guess);
	}

	void addGuess(String guess) {
		guessedLetters += guess;
	}

	boolean wordWasGuessed() {
		if (getNOfUnderscoresLeft() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the word has at least 2 alphabet characters
	 */
	boolean hasValidWordToGuess() {
		return (!wordToGuess.trim().isEmpty()  && wordToGuess.length() > 1 && wordToGuess.matches("[a-zA-Z]+"));
	}

	/**
	 * Returns number of underscores in the word showed to the players Used to see
	 * if the word is guessed (n == 0)
	 */
	private int getNOfUnderscoresLeft() {
		return (int) wordToGuessUnderscore.chars().filter(ch -> ch == '_').count();
	}

	/**
	 * Handles UI of setting the word and checks, if it is valid
	 */
	void setWordToGuess(Scanner scanner, Player gameMaster) {
		boolean validGuessWord = false;
		do {
			Utility.clearScreen();
			System.out.println("Gamemaster: " + gameMaster.getName() + "\nEnter word, that will be guessed:");
			wordToGuess = scanner.nextLine();
			if (hasValidWordToGuess()) {
				validGuessWord = true;
			} else {
				System.out.println("Invalid word entered!");
				scanner.nextLine();
			}
		} while (!validGuessWord);
		Utility.clearScreen();
		System.out.println("You chose:\n" + wordToGuess + "\nEnter \"Y\" to change it, or press Enter to continue");
		if (scanner.nextLine().toLowerCase().equals("y")) {
			wordToGuess = "";
			setWordToGuess(scanner, gameMaster);
		}
		wordToGuess = wordToGuess.toUpperCase();
	}

	/**
	 * Formats the wordToGuess into the initial underscore form to show to players
	 */
	void setDisplayedWordToGuess() {
		for (int i = 0; i < wordToGuess.length(); i++) {
			wordToGuessUnderscore += "_ ";
		}
	}

	/**
	 * Replaces underscores in the word showed to players with letters
	 */
	int updateDisplayedSecretWord(char letterToUpdateWith) {
		char[] DispSecWordArr = wordToGuessUnderscore.toCharArray();
		int nOfReplacements = 0;
		for (int i = 0; i < wordToGuess.length(); i++) {
			if (wordToGuess.charAt(i) == letterToUpdateWith) {
				DispSecWordArr[i * 2] = letterToUpdateWith;
				nOfReplacements++;
			}
		}
		wordToGuessUnderscore = String.valueOf(DispSecWordArr);
		return nOfReplacements;
	}

	/**
	 * Handles user input and ends the round if the tries run out, or the word is
	 * guessed
	 */
	boolean handleGuessing(Scanner scanner, Player currentPlayer, Player currentGameMaster) {
		printGameScreen(currentPlayer);
		System.out.println("Enter a letter, or whole word");
		String guess = scanner.nextLine();
		guess = guess.toUpperCase();
		if (guess.equals("1")) {
			exitRound = true;
		}
		if (guess.length() == 0) {
			System.out.println("Error - empty guess inputed!");
			scanner.nextLine();
		} else if (guess.length() == 1) {
			if (guessedLetters.contains(guess)) {
				System.out.println("Error - This letter was already guessed!");
				scanner.nextLine();
			} else {
				guessedLetters += guess;
				if (getWordToGuess().indexOf(guess.charAt(0)) != -1) {
					currentPlayer.setScoreToAddThisRound(
							currentPlayer.getScoreToAddThisRound() + updateDisplayedSecretWord(guess.charAt(0)));
					if (getWordToGuessUnderscore().indexOf('_') == -1) {
						return true;
					}
				} else {
					nOfTriesLeft--;
				}
			}
		} else {
			if (getWordToGuess().equals(guess)) {
				currentPlayer.setScoreToAddThisRound(currentPlayer.getScoreToAddThisRound() + getNOfUnderscoresLeft());
				wordToGuessUnderscore = wordToGuess;
				return true;
			} else {
				nOfTriesLeft--;
			}

		}
		if (nOfTriesLeft == 0) {
			int nOfUnderscoresRemaining = getNOfUnderscoresLeft();
			currentGameMaster.setScoreToAddThisRound(nOfUnderscoresRemaining);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Helper function printing the state of the game to players
	 * 
	 * @param playerOnTurn
	 */
	private void printGameScreen(Player playerOnTurn) {
		Utility.clearScreen();
		System.out.println("Tries left: " + nOfTriesLeft + "\nSecret word:\n" + getWordToGuessUnderscore()
				+ "\nGuessed letters: " + guessedLetters + "\nTurn of player: " + playerOnTurn.getName());
	}

	/**
	 * Resets all variables in case of round end
	 */
	void resetGameVariables() {
		nOfTriesLeft = defaultNOfTriesLeft;
		wordToGuess = "";
		wordToGuessUnderscore = "";
		guessedLetters = "";
		exitRound = false;
	}
}
