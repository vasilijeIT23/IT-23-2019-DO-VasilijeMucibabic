package app.view;

import app.controller.DrawingController;
import app.drawing.enums.Modes;
import app.geometry.*;
import app.geometry.Point;
import app.geometry.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PnlDrawing extends JPanel {

    private DrawingController controller;

    public PnlDrawing() {
        setBackground(Color.WHITE);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (controller == null) return;
                controller.handleClick(e);
            }
        });
    }


    public void setController(DrawingController controller) {
        this.controller = controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Shape s : controller.getShapesCopy()) {
                if (s instanceof SurfaceShape ss) {
                    ss.fill(g2);
                    ss.draw(g2);
                } else {
                    s.draw(g2);
                }

                if (s.isSelected()) {
                    Color prevColor = g2.getColor();
                    Stroke prevStroke = g2.getStroke();
                    g2.setColor(Color.BLUE);
                    g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                            0, new float[]{6, 4}, 0));
                    s.drawSelected(g2);
                    g2.setColor(prevColor);
                    g2.setStroke(prevStroke);
                }
            }

            if (controller.getMode() == Modes.LINE && controller.getPendingShapeStart() != null) {
                g2.setColor(new Color(120, 120, 120));
                g2.drawString("Line: click end point...", 10, 20);
                Point p = controller.getPendingShapeStart();
                g2.fillOval(p.getX() - 3, p.getY() - 3, 6, 6);
            }
        } finally {
            g2.dispose();
        }
    }
}
