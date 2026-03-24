package app.model;

import app.command.Command;
import app.geometry.Shape;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DrawingModel {
    private final List<Shape> shapes = new ArrayList<>();
    private final Deque<Command> history = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private final List<Command> allCommands = new ArrayList<>();

    public void executeCommand(Command cmd) {
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command cmd = history.pop();
            cmd.undo();
            redoStack.push(cmd); // ← this line is missing
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            history.push(cmd); // back onto history
        }
    }

    public void addShape(Shape s) { shapes.add(s); }
    public void removeShape(Shape s) { shapes.remove(s); }
    public List<Shape> getShapes() { return shapes; }

    public List<Shape> getShapesCopy() {
        return new ArrayList<>(shapes); // NEW list — changes won't affect the model
    }
    public Shape getShape(int i) { return shapes.get(i); }
    public int getSize() { return shapes.size(); }

    public boolean canUndo() { return !history.isEmpty(); }
    public boolean canRedo() { return !redoStack.isEmpty(); }

    public void addCommand(Command c) {
        allCommands.add(c);
    }
}