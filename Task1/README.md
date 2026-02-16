# Tic Tac Toe AI (Java)

This project is a console-based implementation of the classic Tic Tac Toe game developed in Java.  
It features an intelligent AI opponent that uses the **Minimax algorithm**, ensuring optimal and unbeatable gameplay.

The project demonstrates the application of **game theory**, **algorithmic decision-making**, and **efficient state representation** in Java.

---

## Features

- Human vs Computer (AI) gameplay
- AI powered by the Minimax algorithm
- Efficient bitboard-based board representation
- Input validation to prevent illegal moves
- Automatic detection of win, loss, and draw conditions
- Runs directly in the command line / terminal

---

## Technologies Used

- Java (JDK 8 or above)
- Object-Oriented Programming (OOP)
- Minimax Algorithm
- HashMap for memoization

---

## How to Run

1. Compile the program:
   ```bash
   javac TicTacToeAI.java
2. Run the program:

  java TicTacToeAI

# Gameplay Instructions

The game board uses the following numbering system for positions:

0 1 2
3 4 5
6 7 8

- The human player uses X.
- The AI uses O.
- Enter a number between 0 and 8 to make your move.
# Project Highlights

- Uses bitwise operations to efficiently track board states.
- AI evaluates all possible future game states to make optimal decisions.
- Ensures that the AI never loses when played correctly.