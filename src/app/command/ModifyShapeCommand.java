package app.command;

import app.geometry.Shape;

import java.util.List;

public class ModifyShapeCommand implements Command {

    private final List<Shape> shapes;
    private final int index;     // position in the list
    private final Shape before;  // clone of original state
    private final Shape after;   // clone of new state

    public ModifyShapeCommand(List<Shape> shapes, Shape original, Shape modified) {
        this.shapes = shapes;
        this.index  = shapes.indexOf(original); // capture index at creation time
        this.before = original.copy();
        this.after  = modified.copy();
    }

    @Override
    public void execute() {
        if (index != -1) shapes.set(index, after.copy());
    }

    @Override
    public void undo() {
        if (index != -1) shapes.set(index, before.copy());
    }
}