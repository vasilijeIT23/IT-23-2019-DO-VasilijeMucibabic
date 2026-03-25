package app.command;

import app.geometry.Shape;

import java.util.List;

public class RemoveShapeCommand implements Command {

    private final List<Shape> shapes;
    private final Shape shape;
    private final int index;
    private final String description;

    public RemoveShapeCommand(List<Shape> shapes, Shape shape, String description) {
        this.shapes = shapes;
        this.shape  = shape;
        this.index  = shapes.indexOf(shape); // capture position before removal
        this.description = description;
    }

    @Override
    public void execute() {
        shapes.remove(shape);
    }

    @Override
    public void undo() {
        shapes.add(index, shape); // restore at original position
    }

    @Override
    public String getDescription() {
        return "";
    }
}