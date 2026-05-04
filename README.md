# ♟️ Chess Game – Full System Design & Implementation

A complete object-oriented Chess game built in Java, featuring a clean architecture, full rule enforcement, interactive GUI, and an AI opponent powered by Minimax with Alpha-Beta pruning.

---

## 🚀 Features

### 🎮 Core Gameplay

* Fully functional 2-player chess (PvP)
* Player vs Bot mode (PvB)
* Turn-based game management
* Legal move validation (no illegal states)

### ♜ Complete Chess Rules

* ✔ Castling (King-side & Queen-side)
* ✔ En Passant
* ✔ Pawn Promotion (with selection UI)
* ✔ Check / Checkmate detection
* ✔ Stalemate detection

### 🤖 AI Engine

* Minimax algorithm
* Alpha-Beta pruning optimization
* Configurable difficulty levels:

  * Easy (depth 1)
  * Medium (depth 3)
  * Hard (depth 5)

### 🧠 System Design Highlights

* Object-Oriented Architecture
* Separation of Concerns (Model / GUI / AI)
* Observer Pattern for UI updates
* Deep-copy board simulation for AI

### 🖥️ GUI (Swing / JavaFX)

* Interactive 8×8 chess board
* Move highlighting system
* Captured pieces display
* Move history tracking
* Game status indicators (Check, Mate, etc.)
* Control panel (Undo, Resign, Restart, Draw)

---

## 🏗️ Project Structure

```
chess/
│
├── model/
│   ├── pieces/        # Piece hierarchy (Piece, King, Queen, etc.)
│   ├── board/         # Board & Cell logic
│   └── game/          # Game state & turn management
│
├── ai/
│   └── Bot.java       # Minimax + Alpha-Beta AI
│
├── gui/
│   ├── panels/        # Game UI components
│   └── dialogs/       # Settings & interactions
│
└── util/
    ├── MoveHistory
    └── Constants
```

---

## 🧩 Key Components

### 🔹 Board

Handles:

* Piece placement
* Move execution
* Rule enforcement
* Check & checkmate detection

### 🔹 Game

Controls:

* Turn switching
* Game modes (PvP / PvB)
* Game status updates

### 🔹 Piece Hierarchy

Abstract base class with concrete implementations:

* King, Queen, Rook, Bishop, Knight, Pawn

### 🔹 AI Bot

* Generates all legal moves
* Evaluates board states
* Uses Minimax with Alpha-Beta pruning

---

## 🔄 Game Flow

1. Player selects a piece
2. Legal moves are highlighted
3. Player makes a move
4. Game state updates
5. AI responds (in PvB mode)
6. System checks for:

   * Check
   * Checkmate
   * Stalemate

---

## 🧪 Future Improvements

* [ ] Add UML diagrams (Class, Sequence)
* [ ] Improve AI evaluation heuristics
* [ ] Add save/load game functionality
* [ ] Implement multiplayer (network mode)
* [ ] Enhance UI/UX (animations, themes)

---

## 🛠️ Technologies Used

* Java
* Swing / JavaFX
* Object-Oriented Design Principles

---

## 📌 Author

Developed as a full system design + implementation project focusing on clean architecture and complete chess rule handling.

---

## ⭐ Contributing

Contributions, suggestions, and improvements are welcome!

---

## 📜 License

This project is open-source and available under the MIT License.
