# Chess OOP — Full Java Implementation

A complete chess game rebuilt from scratch to showcase every major OOP concept
taught in a Java OOP course.

---

## How to Compile & Run

```bash
# From the project root (where /src lives):
javac -d out $(find src -name "*.java")
java  -cp out chess.Main
```

Requires **Java 11+**. No external libraries.

---

## Package & Class Map

```
chess/
├── Main.java                          Entry point
│
├── model/
│   ├── PieceColor.java                Enum  – WHITE / BLACK
│   ├── PieceType.java                 Enum  – piece types + material values
│   ├── Position.java                  Immutable value object (row, col)
│   ├── Move.java                      Immutable value object – all move types
│   │
│   ├── pieces/
│   │   ├── Piece.java                 Abstract base – Template Method pattern
│   │   ├── Pawn.java                  Forward moves, double push, en passant, promotion
│   │   ├── Knight.java                L-shape jumper
│   │   ├── Bishop.java                Diagonal slider
│   │   ├── Rook.java                  Orthogonal slider
│   │   ├── Queen.java                 Diagonal + orthogonal slider
│   │   ├── King.java                  Single-step + castling
│   │   └── PieceFactory.java          Factory pattern for piece creation
│   │
│   ├── board/
│   │   ├── ChessBoard.java            Interface – read-only view for move generators
│   │   ├── Board.java                 Central mutable game state
│   │   ├── BoardSetup.java            Separates initial placement (SRP)
│   │   ├── MoveExecutor.java          Applies a move to the squares array (SRP)
│   │   ├── MoveValidator.java         Filters pseudo-legal → legal (SRP)
│   │   ├── CastlingRights.java        Tracks castling availability
│   │   └── PositionTables.java        Piece-square tables for evaluation
│   │
│   └── state/
│       └── GameState.java             Enum – PLAYING / CHECK / CHECKMATE / STALEMATE / DRAW
│
├── ai/
│   ├── ChessAI.java                   Facade – async AI wrapper
│   │
│   ├── strategy/
│   │   ├── AIStrategy.java            Strategy interface
│   │   ├── RandomStrategy.java        Easy – prefers captures randomly
│   │   ├── MinimaxStrategy.java       Medium/Hard – minimax + alpha-beta
│   │   └── AIStrategyFactory.java     Factory – creates strategy by difficulty
│   │
│   └── evaluation/
│       ├── BoardEvaluator.java        Strategy interface for evaluation
│       ├── MaterialAndPositionalEvaluator.java  Material + PST evaluation
│       └── MoveScorer.java            MVV-LVA move ordering for alpha-beta
│
├── core/
│   ├── GameController.java            Application controller – Facade + Observer
│   └── GameEventListener.java         Observer interface for UI ↔ model decoupling
│
└── ui/
    ├── ChessWindow.java               Main JFrame – implements GameEventListener
    │
    ├── panels/
    │   ├── BoardPanel.java            Swing board panel – delegates to BoardRenderer
    │   ├── StatusPanel.java           Turn info, game state, move history
    │   └── ControlPanel.java          New game, flip board, difficulty selectors
    │
    ├── dialogs/
    │   ├── PromotionDialog.java       Modal piece-picker for pawn promotion
    │   └── GameOverDialog.java        Modal end-game result + play again
    │
    └── renderer/
        ├── BoardRenderer.java         Orchestrates all board painting
        ├── SquareRenderer.java        Draws individual squares + highlights
        ├── PieceRenderer.java         Draws Unicode chess symbols
        └── ColorTheme.java            Central color constants
```

---

## OOP Concepts Demonstrated

| Concept | Where |
|---|---|
| **Abstraction** | `Piece` abstract class; `AIStrategy`, `BoardEvaluator`, `ChessBoard`, `GameEventListener` interfaces |
| **Inheritance** | `Pawn`, `Knight`, `Bishop`, `Rook`, `Queen`, `King` all extend `Piece` |
| **Polymorphism** | `board.getPieceAt(pos).generatePseudoLegalMoves(…)` — called the same way for every piece type |
| **Encapsulation** | All fields private; state mutated only through explicit methods |
| **Template Method** | `Piece.addSlidingMoves()` / `addStepMove()` used by subclasses |
| **Factory** | `PieceFactory`, `AIStrategyFactory` |
| **Strategy** | `AIStrategy` + `BoardEvaluator` — swappable at runtime |
| **Observer** | `GameEventListener` — controller notifies UI without knowing Swing |
| **Facade** | `GameController` (single entry point for all game actions), `ChessAI` |
| **Value Object** | `Position`, `Move` — immutable, `equals`/`hashCode` defined |
| **Interface segregation** | `ChessBoard` read-only interface used by piece move generators |
| **SRP** | `BoardSetup`, `MoveExecutor`, `MoveValidator` each do one thing |
| **Enums with behaviour** | `PieceType.getMaterialValue()`, `PieceColor.opposite()`, `GameState.isOver()` |
| **Copy constructor** | `Board(Board)`, `CastlingRights(CastlingRights)`, every `Piece` subclass |
| **Generics + Lambdas** | `Consumer<Move>` callback in `ChessAI`, stream in `GameController` |
