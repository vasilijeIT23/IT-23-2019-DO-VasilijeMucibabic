package app.geometry;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Color color;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public abstract boolean same(Shape shape);

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public abstract void draw(Graphics2D g);
    public Color getColor() {
        return color;
    }

    public abstract boolean contains(Point p);


    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void drawSelected(Graphics2D g);

    public abstract Shape copy();
}
