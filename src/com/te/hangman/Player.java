package com.te.hangman;

public class Player {
	private String name;
	private int overallScore;
	private int scoreToAddThisRound;

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getScore() {
		return overallScore;
	}

	void setScore(int score) {
		this.overallScore = score;
	}

	int getScoreToAddThisRound() {
		return scoreToAddThisRound;
	}

	void setScoreToAddThisRound(int score) {
		this.scoreToAddThisRound = score;
	}

	Player(String name) {
		this.name = name;
		overallScore = 0;
	}

	@Override
	public String toString() {
		return name;
	}
}
