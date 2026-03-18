package app.geometry;

import hexagon.Hexagon;
import java.awt.*;

public class HexagonAdapter extends SurfaceShape {

    private final Hexagon hexagon;

    public HexagonAdapter(int x, int y, int radius, Color borderColor, Color fillColor) {
        this.hexagon = new Hexagon(x, y, radius);
        this.hexagon.setBorderColor(borderColor);
        this.hexagon.setAreaColor(fillColor);
        setColor(borderColor);
        setInnerColor(fillColor);
    }

    @Override
    public void draw(Graphics2D g) {
        hexagon.paint(g);
    }

    @Override
    public void fill(Graphics2D g) {
        return;
    }

    @Override
    public boolean contains(Point p) {
        return hexagon.doesContain(p.getX(), p.getY());
    }

    @Override
    public void drawSelected(Graphics2D g) {
        Color prevColor   = g.getColor();
        Stroke prevStroke = g.getStroke();
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6, 4}, 0));

        int r = hexagon.getR() + 6;
        int cx = hexagon.getX();
        int cy = hexagon.getY();

        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            xPoints[i] = (int) (cx + r * Math.cos(angle));
            yPoints[i] = (int) (cy + r * Math.sin(angle));
        }

        g.drawPolygon(xPoints, yPoints, 6);

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    public int getX()      { return hexagon.getX(); }
    public int getY()      { return hexagon.getY(); }
    public int getRadius() { return hexagon.getR(); }
}