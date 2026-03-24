package app.command;

import app.geometry.Shape;

import java.util.List;

public class AddShapeCommand implements Command {
    private final List<Shape> shapes;
    private final Shape shape;

    public AddShapeCommand(List<Shape> shapes, Shape shape) {
        this.shapes = shapes;
        this.shape = shape;
    }

    @Override
    public void execute() {
        shapes.add(shape);
    }

    @Override
    public void undo() {
        shapes.remove(shape);
    }
}