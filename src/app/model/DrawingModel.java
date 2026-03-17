package app.model;

import app.geometry.Shape;

import java.util.ArrayList;
import java.util.List;

public class DrawingModel {
    private final List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape s) { shapes.add(s); }
    public void removeShape(Shape s) { shapes.remove(s); }
    public List<Shape> getShapes() { return shapes; }
    public Shape getShape(int i) { return shapes.get(i); }
    public int getSize() { return shapes.size(); }
}