package app.geometry;

import java.awt.*;
import java.util.ArrayList;

public class Line extends Shape {
    private final Point a;
    private final Point b;

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public Line(Point a, Point b, Color color) {
        this(a, b);
        setColor(color);
    }

    public static double distancePointToLine(Point a, Point b, Point x) {
        int A = b.getY() - a.getY();
        int B = a.getX() - b.getX();
        int C = b.getX() * a.getY() - a.getX() * b.getY();

        return Math.abs(A * x.getX() + B * x.getY() + C) / Math.sqrt(A * A + B * B);
    }

    @Override
    public boolean contains(Point p) {
        return distancePointToLine(a, b, p) <= 3;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    @Override
    public void drawSelected(Graphics2D g) {
        g.drawLine(getA().getX(), getA().getY(), getB().getX(), getB().getY());
    }
}
