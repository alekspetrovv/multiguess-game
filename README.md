# Geektastic: Word Reveal Code Challenge Solution

This project contains the complete solution for the Geektastic "Word Reveal" code challenge. It implements the backend logic for a multiplayer, concurrent word guessing game as per the detailed specification.

## Overview

The core of this project is a thread-safe Java implementation of a word guessing game. The game initializes a list of words of the same length, hiding all but one random character in each. Players can submit guesses concurrently. The system validates these guesses, handles scoring for both partial and exact matches, and updates the game state accordingly.

The solution includes the game logic, an improved vocabulary checker, and a comprehensive suite of deterministic unit tests to verify correctness.

## Features

* **Concurrent Game Logic:** The game is designed to handle simultaneous guess submissions from multiple players using `synchronized` methods to ensure thread safety.
* **Dynamic Game Initialization:** Each game starts by revealing one randomly selected character from each word in the list.
* **Complex Guess Validation:** Submissions are validated based on:
    * Correct word length.
    * Existence in a provided vocabulary (`wordlist.txt`).
    * Consistency with already revealed characters on the game board.
    * Ensuring the guess is not for an already completed word.
* **Dual Scoring System:**
    * **Partial Match:** Scores points based on the number of newly revealed characters across *all* words in the game.
    * **Exact Match:** Scores a flat 10 points and reveals only the matched word, leaving others unchanged.
* **Testable Design:** The implementation uses dependency injection for the `Random` object, allowing for fully deterministic unit tests.

## Project Structure

The project follows a standard Maven directory layout.