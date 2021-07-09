package com.te.hangman;

import java.util.Scanner;

public class WordManager {
	private String wordToGuess = "";
	private String wordToGuessUnderscore = "";
	private int defaultNOfTriesLeft = 15;
	private int nOfTriesLeft = defaultNOfTriesLeft;
	private String guessedLetters = "";

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

	void setWordToGuess(Scanner scanner, Player gameMaster) {
		boolean validGuessWord = false;
		do {
			Utility.clearScreen();
			System.out.println("Gamemaster: " + gameMaster.getName() + "\nEnter word, that will be guessed:");
			wordToGuess = scanner.nextLine();
			if (hasValidWordToGuess() && wordToGuess.length() > 1 && wordToGuess.matches("[a-zA-Z]+")) {
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

	boolean handleGuessing(Scanner scanner, Player currentPlayer, Player currentGameMaster) {
		printGameScreen(currentPlayer);
		System.out.println("Enter a letter, or whole word");
		String guess = scanner.nextLine();
		guess = guess.toUpperCase();
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
					currentPlayer.setScoreToAddThisRound(currentPlayer.getScoreToAddThisRound() + updateDisplayedSecretWord(guess.charAt(0)));
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

	private void printGameScreen(Player playerOnTurn) {
		Utility.clearScreen();
		System.out.println("Tries left: " + nOfTriesLeft + "\nSecret word:\n" + getWordToGuessUnderscore()
				+ "\nGuessed letters: " + guessedLetters
				+ "\nTurn of player: " + playerOnTurn.getName());
	}

	void setDisplayedWordToGuess() {
		for (int i = 0; i < wordToGuess.length(); i++) {
			wordToGuessUnderscore += "_ ";
		}
	}

	void resetGameVariables() {
		nOfTriesLeft = defaultNOfTriesLeft;
		wordToGuess = "";
		wordToGuessUnderscore = "";
		guessedLetters = "";
	}

	boolean wordWasGuessed() {
		if (getNOfUnderscoresLeft() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	boolean hasValidWordToGuess()
	{
		return !wordToGuess.trim().isEmpty();
	}

	private int getNOfUnderscoresLeft() {
		return (int) wordToGuessUnderscore.chars().filter(ch -> ch == '_').count();
	}
}
