package app.model;

import app.command.Command;
import app.command.RedoCommand;
import app.command.UndoCommand;
import app.geometry.Shape;
import app.observer.SelectionObserver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DrawingModel {
    private final List<Shape> shapes = new ArrayList<>();
    private final Deque<Command> history = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private final List<Command> allCommands = new ArrayList<>();
    private final List<Shape> selectedShapes = new ArrayList<>();
    private final List<SelectionObserver> selectionObservers = new ArrayList<>();

    public void addSelectionObserver(SelectionObserver observer) {
        selectionObservers.add(observer);
    }

    public void notifySelectionChanged() {
        long count = shapes.stream().filter(Shape::isSelected).count();
        selectionObservers.forEach(o -> o.onSelectionChanged((int) count));
    }

    public void executeCommand(Command cmd) {
        cmd.execute();
        history.push(cmd);
        allCommands.add(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command cmd = history.pop();
            cmd.undo();
            redoStack.push(cmd);
            allCommands.add(new UndoCommand(cmd));
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            history.push(cmd);
            allCommands.add(new RedoCommand(cmd));
        }
    }

    public void addSelected(Shape s) { selectedShapes.add(s); }
    public boolean hasOne(Shape s) { return selectedShapes.size() == 1; }

    public Shape getSelectedShape() {
        return shapes.stream()
                .filter(Shape::isSelected)
                .findFirst()
                .orElse(null);
    }

    public void addShape(Shape s) { shapes.add(s); }
    public void removeShape(Shape s) { shapes.remove(s); }
    public void emptyList() { shapes.clear(); }
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
    public void clearLog() { allCommands.clear(); }

    public List<Command> getAllCommands() {
        return allCommands;
    }

}