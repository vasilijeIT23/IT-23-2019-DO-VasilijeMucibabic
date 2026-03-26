package app.geometry;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public abstract class SurfaceShape extends Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private Color innerColor;

    public abstract void fill(Graphics2D g);

    public Color getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

}
