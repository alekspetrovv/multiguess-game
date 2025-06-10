package com.codesse.multiguess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WordGameImpl implements MultiplayerGuessingGame {

    private final List<String> targetWords;
    private final List<StringBuilder> gameWords;
    private final VocabularyChecker vocabularyChecker;
    private final int wordLength;
    private final Random random;

    public WordGameImpl(List<String> words, VocabularyChecker vocabularyChecker, Random random) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Word list cannot be null or empty.");
        }

        this.wordLength = words.get(0).length();
        if (wordLength == 0 || !words.stream().allMatch(w -> w.length() == wordLength)) {
            throw new IllegalArgumentException("All words must be of the same non-zero length.");
        }

        this.vocabularyChecker = vocabularyChecker;
        this.targetWords = new ArrayList<>(words);
        this.random = random;
        this.gameWords = initializeGameWords();
    }

    private List<StringBuilder> initializeGameWords() {
        return targetWords.stream()
                .map(word -> {
                    StringBuilder hiddenWord = new StringBuilder("*".repeat(wordLength));
                    int revealIndex = random.nextInt(wordLength);
                    hiddenWord.setCharAt(revealIndex, word.charAt(revealIndex));
                    return hiddenWord;
                })
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<String> getGameStrings() {
        return gameWords.stream()
                .map(StringBuilder::toString)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized int submitGuess(String playerName, String submission) {
        if (submission == null || submission.length() != wordLength || !vocabularyChecker.exists(submission)) {
            return 0;
        }

        boolean isValidAsGuess = false;
        for (int i = 0; i < gameWords.size(); i++) {
            if (!isFullyRevealed(i) && isSubmissionValidForWord(submission, gameWords.get(i))) {
                isValidAsGuess = true;
                break;
            }
        }
        if (!isValidAsGuess) {
            return 0;
        }

        for (int i = 0; i < targetWords.size(); i++) {
            if (targetWords.get(i).equals(submission)) {
                if (isFullyRevealed(i)) {
                    return 0;
                }
                gameWords.set(i, new StringBuilder(submission));
                return 10;
            }
        }

        int newlyRevealedCount = 0;
        for (int i = 0; i < targetWords.size(); i++) {
            String target = targetWords.get(i);
            StringBuilder current = gameWords.get(i);
            for (int j = 0; j < wordLength; j++) {
                if (target.charAt(j) == submission.charAt(j) && current.charAt(j) == '*') {
                    current.setCharAt(j, submission.charAt(j));
                    newlyRevealedCount++;
                }
            }
        }
        return newlyRevealedCount;
    }

    private boolean isFullyRevealed(int wordIndex) {
        return gameWords.get(wordIndex).indexOf("*") == -1;
    }

    private boolean isSubmissionValidForWord(String submission, StringBuilder partialWord) {
        for (int i = 0; i < wordLength; i++) {
            char partialChar = partialWord.charAt(i);
            if (partialChar != '*' && partialChar != submission.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}