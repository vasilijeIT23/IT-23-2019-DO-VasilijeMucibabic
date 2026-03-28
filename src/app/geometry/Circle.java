package app.geometry;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serial;
import java.io.Serializable;

public class Circle extends SurfaceShape implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

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
    public boolean same(Shape shape) {
        if (shape instanceof Circle check) {
            return this.center == check.getCenter() && check.getRadius() == this.radius &&
                    this.getColor().equals(check.getColor()) && this.getInnerColor().equals(check.getInnerColor());
        }
        return false;
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

    @Override
    public Shape copy() {
        return new Circle(
                new Point(center.getX(), center.getY()),
                radius,
                getColor(),
                getInnerColor()
        );
    }

    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }
}