package app.geometry;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Circle extends SurfaceShape {

    private final Point center;
    private final int radius;

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(Point center, int radius, Color color) {
        this(center, radius);
        setColor(color);
    }

    public Circle(Point center, int radius, Color color, Color innerColor) {
        this(center, radius, color);
        setInnerColor(innerColor);
    }

    @Override
    public boolean contains(Point p) {
        if (this instanceof Donut d){
            return center.distanceTo(p) <= radius + 2 && center.distanceTo(p) >= d.getInnerRadius() - 2;
        }
        else {
            return center.distanceTo(p) <= radius + 2;
        }
    }


    @Override
    public void fill(Graphics2D g) {
        g.setColor(getInnerColor());
        g.fill(getArea());
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.drawOval(
                center.getX() - radius,
                center.getY() - radius,
                radius * 2,
                radius * 2
        );
    }

    protected Area getArea() {
        return new Area(new Ellipse2D.Double(
                center.getX() - radius,
                center.getY() - radius,
                radius * 2,
                radius * 2
        ));
    }

    @Override
    public void drawSelected(Graphics2D g) {
        int r = getRadius() + 6;
        g.drawOval(getCenter().getX() - r, getCenter().getY() - r, r * 2, r * 2);
    }


    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }
}