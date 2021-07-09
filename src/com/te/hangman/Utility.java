package com.te.hangman;

public final class Utility {
	/**
	 * Clears the screen in a Windows console
	 */
	static void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception E) {
			System.out.println(E);
		}
	}
}
