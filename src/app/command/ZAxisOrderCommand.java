// app/command/ZOrderCommand.java
package app.command;

import app.geometry.Shape;

import java.util.List;

public class ZAxisOrderCommand implements Command {

    public enum ZOperation {
        TO_FRONT, TO_BACK, BRING_TO_FRONT, BRING_TO_BACK
    }

    private final List<Shape> shapes;
    private final Shape shape;
    private final ZOperation operation;
    private int originalIndex;

    public ZAxisOrderCommand(List<Shape> shapes, Shape shape, ZOperation operation, String description) {
        this.shapes = shapes;
        this.shape = shape;
        this.operation = operation;
        this.description = description;
        this.originalIndex = shapes.indexOf(shape); // capture before move
    }

    private final String description;

    @Override
    public void execute() {
        int current = shapes.indexOf(shape);
        shapes.remove(current);

        switch (operation) {
            case TO_FRONT -> shapes.add(Math.min(current + 1, shapes.size()), shape);
            case TO_BACK  -> shapes.add(Math.max(current - 1, 0), shape);
            case BRING_TO_FRONT -> shapes.add(shapes.size(), shape); // top of list = drawn last = on top
            case BRING_TO_BACK  -> shapes.add(0, shape);             // bottom of list = drawn first = behind
        }
    }

    @Override
    public void undo() {
        shapes.remove(shape);
        shapes.add(originalIndex, shape); // restore exact original position
    }

    @Override
    public String getDescription() {
        return description;
    }
}