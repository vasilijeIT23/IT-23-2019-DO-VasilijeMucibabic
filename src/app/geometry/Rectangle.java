package app.geometry;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Rectangle extends SurfaceShape implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
    private final Point upperLeftPoint;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getUpperLeftPoint() {
        return upperLeftPoint;
    }

    private final int height;
    private final int width;

    public Rectangle(Point upperLeftPoint, int height, int width) {
        this.upperLeftPoint = upperLeftPoint;
        this.height = height;
        this.width = width;
    }

    public Rectangle(Point upperLeftPoint, int height, int width, Color color) {
        this(upperLeftPoint, height, width);
        setColor(color);
    }

    public Rectangle(Point upperLeftPoint, int height, int width, Color color, Color innerColor) {
        this(upperLeftPoint, height, width, color);
        setInnerColor(innerColor);
    }

    @Override
    public boolean contains(Point p) {
        return upperLeftPoint.getX() - 2 <= p.getX() && p.getX() <= upperLeftPoint.getX() + width + 2 &&
                upperLeftPoint.getY() - 2 <= p.getY() && p.getY() <= upperLeftPoint.getY() + height + 2;
    }

    @Override
    public boolean same(Shape shape) {
        if (shape instanceof Rectangle check) {
            return upperLeftPoint == check.upperLeftPoint && check.height == height && check.width == width
                    && this.getColor().equals(check.getColor()) && this.getInnerColor().equals(check.getInnerColor());
        }
        return false;
    }

    @Override
    public void fill(Graphics2D g) {
        g.setColor(getInnerColor());
        g.fillRect(this.upperLeftPoint.getX() + 1, this.upperLeftPoint.getY() + 1,
                width - 1, height - 1);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.drawRect(upperLeftPoint.getX(), upperLeftPoint.getY(), width, height);
    }

    @Override
    public void drawSelected(Graphics2D g) {
        g.drawRect(getUpperLeftPoint().getX() - 4, getUpperLeftPoint().getY() - 4,
                getWidth() + 8, getHeight() + 8);
    }

    @Override
    public Shape copy() {
        return new Rectangle(getUpperLeftPoint(), getHeight(), getWidth(), getColor(), getInnerColor());
    }
}
