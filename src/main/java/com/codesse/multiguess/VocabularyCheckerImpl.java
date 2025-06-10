package com.codesse.multiguess;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VocabularyCheckerImpl implements VocabularyChecker {

    private static final Set<String> vocabulary;

    static {
        Set<String> tempWords = new HashSet<>();
        String resourcePath = "/wordlist.txt";

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(VocabularyCheckerImpl.class.getResourceAsStream(resourcePath), StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    tempWords.add(line.trim().toLowerCase());
                }
            } else {
                throw new RuntimeException("Vocabulary file not found or is empty at " + resourcePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load vocabulary from " + resourcePath, e);
        }
        vocabulary = Collections.unmodifiableSet(tempWords);
    }

    @Override
    public boolean exists(String word) {
        if (word == null) {
            return false;
        }
        return vocabulary.contains(word.toLowerCase());
    }
}