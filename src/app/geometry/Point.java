package app.geometry;

import java.awt.*;

public class Point extends Shape {

    private int x;
    private int y;

    public Point() {

    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, Color color) {
        this(x, y);
        setColor(color);
    }

    public double distanceTo(Point other) {
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean contains(Point p) {
        return x + 2 >= p.getX() && p.getX() >= x - 2 && y + 2 >= p.getY() && p.getY() >= y - 2;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.fillOval(x - 2, y - 2, 5, 5);
    }

    public void drawSelected(Graphics2D g) {
        g.drawOval(getX() - 6, getY() - 6, 12, 12);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

