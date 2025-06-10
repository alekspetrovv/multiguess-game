package com.codesse.multiguess;

import java.util.List;

public interface MultiplayerGuessingGame {

    /**
     * Get the complete list of Strings to display to the players of the game.
     * Represent "hidden" characters with an asterisk *.
     * Completely solved / revealed words should also be included in the list.
     *
     * @return the list of Strings
     */
    List<String> getGameStrings();

    /**
     * Update the game based on a guess submitted by a player and return a score for the submission.
     *
     * @param playerName the name of the player. Player names can be considered as unique. There is no need to implement code to enforce uniqueness.
     * @param submission the word submitted by the player as a guess for partially hidden words in the game.
     * @return a score for the submission equivalent to the number of hidden characters that were caused to be revealed or 10 if the submitted word is an exact match for one of the words in the game. If the submission is invalid for any reason return a score of 0.
     */
    int submitGuess(String playerName, String submission);
}
