package com.codesse.multiguess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MultiplayerGuessingGameTests {

    private MultiplayerGuessingGame game;
    private final VocabularyChecker checker = new VocabularyCheckerImpl();

    private static class MockRandom extends Random {
        private final Queue<Integer> numbersToReturn = new LinkedList<>();

        public MockRandom(List<Integer> numbers) {
            this.numbersToReturn.addAll(numbers);
        }

        @Override
        public int nextInt(int bound) {
            if (numbersToReturn.isEmpty()) {
                return 0;
            }
            return numbersToReturn.poll();
        }
    }

    @BeforeEach
    void setUp() {
        List<String> words = List.of("poker", "cover", "pesto");
        Random predictableRandom = new MockRandom(List.of(2, 4, 0));
        game = new WordGameImpl(words, checker, predictableRandom);
    }

    @Test
    void testInitialGameState() {
        List<String> gameStrings = game.getGameStrings();
        assertEquals(3, gameStrings.size());
        assertEquals("**k**", gameStrings.get(0));
        assertEquals("****r", gameStrings.get(1));
        assertEquals("p****", gameStrings.get(2));
    }

    @Test
    void testInvalidGuessWrongLength() {
        int score = game.submitGuess("player1", "dinner");
        assertEquals(0, score);
    }

    @Test
    void testInvalidGuessNotInVocabulary() {
        int score = game.submitGuess("player1", "zyxwz");
        assertEquals(0, score);
    }

    @Test
    void testValidGuessRevealsLettersAndScores() {
        int score = game.submitGuess("player1", "poser");

        assertEquals(7, score);

        List<String> stateAfterGuess = game.getGameStrings();
        assertEquals("poker", stateAfterGuess.get(0));
        assertEquals("*o*er", stateAfterGuess.get(1));
        assertEquals("p*s**", stateAfterGuess.get(2));
    }
    @Test
    void testExactMatchScoresTenAndRevealsOnlyThatWord() {
        List<String> initialStrings = game.getGameStrings();
        int score = game.submitGuess("player1", "poker");
        assertEquals(10, score);
        List<String> finalStrings = game.getGameStrings();
        assertEquals("poker", finalStrings.get(0));
        assertEquals(initialStrings.get(1), finalStrings.get(1));
        assertEquals(initialStrings.get(2), finalStrings.get(2));
    }

    @Test
    void testGuessingAFullyRevealedWordIsInvalid() {
        game.submitGuess("player1", "pesto");
        List<String> stateAfterFirstGuess = game.getGameStrings();
        assertEquals("pesto", stateAfterFirstGuess.get(2));
        int score = game.submitGuess("player2", "pesto");
        assertEquals(0, score);
    }
}