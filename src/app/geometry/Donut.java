package app.geometry;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;



public class Donut extends Circle {

    private final int innerRadius;

    public Donut(Point center, int radius, int innerRadius) {
        super(center, radius);
        this.innerRadius = innerRadius;
    }

    public Donut(Point center, int radius, int innerRadius, Color color) {
        this(center, radius, innerRadius);
        setColor(color);
    }

    public Donut(Point center, int radius, int innerRadius, Color color, Color innerColor) {
        this(center, radius, innerRadius, color);
        setInnerColor(innerColor);

    }

    // Donut.java
    @Override
    public void fill(Graphics2D g) {
        Area area = super.getArea(); // outer circle area
        area.subtract(new Area(new Ellipse2D.Double(
                getCenter().getX() - innerRadius,
                getCenter().getY() - innerRadius,
                innerRadius * 2,
                innerRadius * 2
        )));
        g.setColor(getInnerColor());
        g.fill(area);
    }

    public void draw(Graphics2D g) {
        super.draw(g); // draws the outer circle
        g.setColor(getColor());
        g.drawOval(
                getCenter().getX() - innerRadius,
                getCenter().getY() - innerRadius,
                innerRadius * 2,
                innerRadius * 2
        );
    }

    @Override
    public void drawSelected(Graphics2D g) {
        super.drawSelected(g); // outer highlight
        int ri = getInnerRadius() - 6;
        g.drawOval(getCenter().getX() - ri, getCenter().getY() - ri, ri * 2, ri * 2);
    }

    public int getInnerRadius() {
        return innerRadius;
    }
}