# ♟️ Chess Game — ByteKnights

> A fully-featured two-player chess application with an AI opponent, built in Java using Swing/JavaFX.

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java)
![OOP](https://img.shields.io/badge/Course-Object--Oriented%20Programming-blue)
![Year](https://img.shields.io/badge/Year-2024-lightgrey)
![License](https://img.shields.io/badge/License-MIT-green)

---

## 👥 Team

**Group:** ByteKnights &nbsp;|&nbsp; **Course:** Object-Oriented Programming 2024 &nbsp;|&nbsp; **Instructor:** Hailemelekot D.

| # | Name | Student ID  |
|---|------|------------|
| 1 | Abere Chanie | UGR/0121/17 |
| 2 | Muluken Seifu | UGR/7376/17 |
| 3 | Siyane Solomon | UGR/5908/17 |
| 4 | Yomiyu Adisu | UGR/0197/17 |

---

## 📖 Overview

This project implements a complete chess game following standard chess rules. It supports two modes — **Player vs Player** and **Player vs Bot** — with an AI engine powered by Minimax with Alpha-Beta pruning. The architecture strictly follows object-oriented principles: inheritance, encapsulation, separation of concerns, and the Observer pattern.

---

## ✨ Features

- ♟️ Full chess rules — castling, en passant, pawn promotion, check/checkmate/stalemate detection
- 🤖 AI opponent with three difficulty levels (Easy / Medium / Hard)
- 🔄 Undo/redo move history with algebraic notation display
- 🎨 Multiple board themes (Classic / Wood / Dark)
- ⏱️ Optional game clock with configurable time per player
- 🔊 Sound effects for moves and captures
- 🏳️ Resign and draw offer support

---

## 🏗️ Architecture

The project is organized into five packages:

```
chess/
├── model/
│   ├── pieces/     # Piece hierarchy — abstract Piece + 6 concrete classes
│   └── board/      # Cell, Board, move logic
├── model/game/     # Game, GameState, turn management
├── ai/             # Bot — Minimax with Alpha-Beta pruning
├── gui/            # All Swing/JavaFX panels and dialogs
└── util/           # Constants, MoveHistory, MoveRecord
```

### Class Hierarchy

```
Piece (abstract)
├── Rook
├── Bishop
├── Knight
├── Pawn
├── Queen
└── King
```

### Key Design Patterns

| Pattern | Where used |
|---------|-----------|
| **Inheritance** | All pieces extend the abstract `Piece` base class |
| **Observer** | `Board` notifies `GamePanel` via `BoardListener` on every state change |
| **Strategy** | `Bot` uses a pluggable `BoardEvaluator` for heuristic scoring |
| **Command** | `MoveRecord` captures full move state for undo/redo |

---

## 🖥️ GUI Flow

```
Application Launch
       │
  Main Menu Panel
       │
     Mode?
    /      \
 PvP        PvB (select difficulty)
    \      /
   Game Panel
  (Board + Side + Controls)
       │
   Game Over?
   /        \
 Yes          No → Continue Playing
   │
Result Dialog (Win / Draw / Resign)
   │
Play Again?
 /       \
Yes       No → Main Menu
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- A Java IDE (IntelliJ IDEA, Eclipse, or VS Code with Java Extension Pack)

### Clone & Run

```bash
git clone https://github.com/muleseifu/Chess.git
cd Chess
```

**Compile:**
```bash
javac -sourcepath src -d out src/chess/Main.java
```

**Run:**
```bash
java -cp out chess.Main
```

Or simply open the project in your IDE and run `Main.java`.

---

## 🎮 How to Play

1. Launch the application — the **Main Menu** appears.
2. Choose **Play vs Person** for two human players, or **Play vs Bot** and select a difficulty.
3. Click a piece to select it — legal moves are highlighted on the board.
4. Click a highlighted square to move.
5. Special moves (castling, en passant, pawn promotion) are handled automatically.
6. Use the **Control Panel** to undo, resign, offer a draw, or return to the menu.

---

## 🤖 AI Engine

The `Bot` class (in `chess.ai`) uses **Minimax with Alpha-Beta pruning**:

| Difficulty | Search Depth |
|------------|-------------|
| Easy | 1 ply |
| Medium | 3 ply |
| Hard | 5 ply |

The evaluation function scores positions based on material balance, piece-square position tables, and mobility. The bot always auto-promotes pawns to Queen.

---

## ♟️ Special Rules

**Castling** — Available when neither the king nor rook has moved, the path between them is clear, and the king is not in check or passing through an attacked square.

**En Passant** — A pawn that advances two squares sets an `enPassantVulnerable` flag for exactly one turn, allowing adjacent enemy pawns to capture diagonally.

**Pawn Promotion** — When a pawn reaches the opposite back rank, a dialog lets the player choose Queen, Rook, Bishop, or Knight.

**Check Detection** — After every move, `Board.isInCheck()` tests whether any enemy piece's move list includes the king's cell. Moves that leave the own king in check are filtered out before being offered as legal moves.

---

## 📁 Project Structure

```
Chess/
├── src/
│   └── chess/
│       ├── Main.java
│       ├── model/
│       │   ├── pieces/         # Piece.java, Rook.java, Bishop.java, ...
│       │   └── board/          # Cell.java, Board.java
│       ├── model/game/         # Game.java, GameMode.java, GameStatus.java
│       ├── ai/                 # Bot.java, BoardEvaluator.java
│       ├── gui/                # MainMenuPanel.java, GamePanel.java, ...
│       └── util/               # MoveHistory.java, MoveRecord.java, Constants.java
├── resources/
│   └── images/                 # Piece sprites (PNG)
├── docs/
│   └── Chess_Design_Document.pdf
└── README.md
```

---

## 📄 Documentation

The full system design document — including UML class diagrams, method specifications, interaction sequences, and component architecture — is available in [`docs/Chess_Design_Document.pdf`](docs/Chess_Design_Document.pdf).

---

## 📜 License

This project was created for academic purposes as part of the **Object-Oriented Programming** course (2024). Feel free to use it for learning.
