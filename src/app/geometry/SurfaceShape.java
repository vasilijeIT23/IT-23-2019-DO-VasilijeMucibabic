package app.geometry;

import java.awt.*;

public abstract class SurfaceShape extends Shape {

    private Color innerColor;

    public abstract void fill(Graphics2D g);

    public Color getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

}
