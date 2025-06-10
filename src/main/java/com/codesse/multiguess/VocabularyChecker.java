package com.codesse.multiguess;

public interface VocabularyChecker {

    /**
     * @param word the word to check against the collection of valid words
     * @return true if the collection contains the word
     */
    boolean exists(String word);

}
