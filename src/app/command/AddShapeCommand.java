package app.command;

import app.geometry.Shape;

import java.util.List;

public class AddShapeCommand implements Command {
    private final List<Shape> shapes;
    private final Shape shape;
    private final String description;

    public AddShapeCommand(List<Shape> shapes, Shape shape, String description) {
        this.shapes = shapes;
        this.shape = shape;
        this.description = description;
    }

    @Override
    public void execute() {
        shapes.add(shape);
    }

    @Override
    public void undo() {
        shapes.remove(shape);
    }

    @Override
    public String getDescription() {
        return description;
    }
}