package app.geometry;

import java.awt.*;

public abstract class Shape {
    private Color color;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

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
