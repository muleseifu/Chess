package Chess.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * MoveHistory – maintains the ordered stack of executed moves
 * along with a redo stack for forward replay.
 */
public class MoveHistory {

    private Deque<MoveRecord> moves     = new ArrayDeque<>();
    private Deque<MoveRecord> redoStack = new ArrayDeque<>();

    // -------------------------------------------------------------------------
    // Core operations
    // -------------------------------------------------------------------------

    /** Record a new move (clears redo stack). */
    public void push(MoveRecord move) {
        moves.push(move);
        redoStack.clear(); // any new move invalidates the redo history
    }

    /** Retrieve and remove the last move. */
    public MoveRecord pop() {
        if (moves.isEmpty()) return null;
        MoveRecord record = moves.pop();
        redoStack.push(record);
        return record;
    }

    /** View the last move without removing it. */
    public MoveRecord peek() {
        return moves.isEmpty() ? null : moves.peek();
    }

    /** Returns true if there is no move history. */
    public boolean isEmpty() {
        return moves.isEmpty();
    }

    /** Wipe history for a new game. */
    public void clear() {
        moves.clear();
        redoStack.clear();
    }

    // -------------------------------------------------------------------------
    // Display
    // -------------------------------------------------------------------------

    /**
     * Returns a list of algebraic notation strings for all recorded moves,
     * oldest first.
     */
    public List<String> getMoveList() {
        List<String> list = new ArrayList<>();
        // moves is a stack (most recent first), so reverse for display
        MoveRecord[] arr = moves.toArray(new MoveRecord[0]);
        for (int i = arr.length - 1; i >= 0; i--) {
            list.add(arr[i].toString());
        }
        return list;
    }

    /** Returns how many moves have been made. */
    public int size() {
        return moves.size();
    }

    /** Returns true if redo is possible. */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /** Pop from redo stack and push back to moves. */
    public MoveRecord redo() {
        if (redoStack.isEmpty()) return null;
        MoveRecord record = redoStack.pop();
        moves.push(record);
        return record;
    }
}
