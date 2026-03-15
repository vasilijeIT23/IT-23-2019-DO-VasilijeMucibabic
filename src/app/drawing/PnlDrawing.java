package app.drawing;

import app.drawing.components.BasicFormDialog;
import app.drawing.dialogs.DlgCirc;
import app.drawing.dialogs.DlgDonut;
import app.drawing.dialogs.DlgRect;
import app.drawing.enums.Modes;
import app.geometry.*;
import app.geometry.Point;
import app.geometry.Rectangle;
import app.geometry.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static app.drawing.components.FormLibrary.*;

public class PnlDrawing extends JPanel {

    private Modes mode = Modes.POINT;

    List<Shape> shapes = new ArrayList<>();
    private Point pendingShapeStart = null;

    private Color borderColor = Color.BLACK;
    private Color innerColor = Color.WHITE;

    public PnlDrawing() {
        setBackground(Color.WHITE);
        setOpaque(true);

        // Nice cursor for drawing
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (mode == Modes.POINT) {
                    var params = BasicFormDialog.ask(
                            SwingUtilities.getWindowAncestor(e.getComponent()),
                            CREATE_POINT_FIELDS
                    );
                    if (params == null) return;
                    Color pointColor   = (Color) params.get("border");
                    shapes.add(new Point(e.getX(), e.getY(), pointColor));
                    repaint();
                    return;
                }

                if (mode == Modes.LINE) {
                    if (pendingShapeStart == null) {
                        pendingShapeStart = new Point(e.getX(), e.getY());
                        repaint();
                    } else {
                        var params = BasicFormDialog.ask(
                                SwingUtilities.getWindowAncestor(e.getComponent()),
                                CREATE_LINE_FIELDS
                        );
                        if (params == null) return;
                        Color lineColor   = (Color) params.get("border");
                        shapes.add(new Line(pendingShapeStart, new Point(e.getX(), e.getY()), lineColor));
                        pendingShapeStart = null;
                        repaint();
                    }
                }

                if (mode == Modes.RECTANGLE) {
                    var params = BasicFormDialog.ask(
                            SwingUtilities.getWindowAncestor(e.getComponent()),
                            CREATE_RECTANGLE_FIELDS
                    );
                    if (params == null) return;

                    int width = (Integer) params.get("width");
                    int height = (Integer) params.get("height");
                    Color border = (Color) params.get("border");
                    Color fill   = (Color) params.get("fill");
                    shapes.add(new Rectangle(new Point(e.getX(), e.getY()), height, width, border, fill));
                    repaint();
                }

                if (mode == Modes.CIRCLE) {
                    var params = BasicFormDialog.ask(
                            SwingUtilities.getWindowAncestor(e.getComponent()),
                            CREATE_CIRCLE_FIELDS
                    );
                    if (params == null) return;

                    int radius = (Integer) params.get("radius");
                    Color border = (Color) params.get("border");
                    Color fill   = (Color) params.get("fill");
                    shapes.add(new Circle(new Point(e.getX(), e.getY()), radius, border, fill));
                    repaint();
                }

                if (mode == Modes.DONUT) {
                    var params = BasicFormDialog.ask(
                            SwingUtilities.getWindowAncestor(e.getComponent()),
                            CREATE_DONUT_FIELDS
                    );
                    if (params == null) return;

                    int inner = (Integer) params.get("inner");
                    int outer = (Integer) params.get("outer");
                    Color border = (Color) params.get("border");
                    Color fill   = (Color) params.get("fill");
                    shapes.add(new Donut(new Point(e.getX(), e.getY()), outer, inner, border, fill));
                    repaint();
                }

                if (mode == Modes.SELECT) {
                    // deselect all first
                    shapes.forEach(s -> s.setSelected(false));

                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        Shape s = shapes.get(i);
                        if (s.contains(new Point(e.getX(), e.getY()))) {
                            s.setSelected(true);
                            break;
                        }
                    }
                    repaint();
                }
                if (mode == Modes.MODIFY) {
                    Object selected = null;

                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        Shape s = shapes.get(i);

                        if (s.contains(new Point(e.getX(), e.getY())) && s.isSelected()) {
                            selected = s;
                            break;
                        }
                    }
                    if (selected == null) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(PnlDrawing.this),
                                "No shape selected. Select a shape first.",
                                "Nothing to modify",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    if (selected instanceof Point) {
                        var params = BasicFormDialog.ask(
                                SwingUtilities.getWindowAncestor(e.getComponent()),
                                MODIFY_POINT_FIELDS
                        );
                        if (params == null) return;

                        int x = (Integer) params.get("x");
                        int y = (Integer) params.get("y");
                        Color border = (Color) params.get("border");
                        shapes.remove(selected);
                        shapes.add(new Point(x, y, border));
                        repaint();
                    }
                    else if (selected instanceof Line) {
                        var params = BasicFormDialog.ask(
                                SwingUtilities.getWindowAncestor(e.getComponent()),
                                MODIFY_LINE_FIELDS
                        );
                        if (params == null) return;

                        int x = (Integer) params.get("x");
                        int y = (Integer) params.get("y");
                        int x2 = (Integer) params.get("x2");
                        int y2 = (Integer) params.get("y2");
                        Color border = (Color) params.get("border");
                        shapes.remove(selected);
                        shapes.add(new Line(new Point(x, y), new Point(x2, y2), border));
                        repaint();
                    }
                    else if (selected instanceof Rectangle) {
                        var params = BasicFormDialog.ask(
                                SwingUtilities.getWindowAncestor(e.getComponent()),
                                MODIFY_RECTANGLE_FIELDS
                        );
                        if (params == null) return;

                        int width = (Integer) params.get("width");
                        int height = (Integer) params.get("height");
                        Color border = (Color) params.get("border");
                        Color fill   = (Color) params.get("fill");
                        shapes.remove(selected);
                        shapes.add(new Rectangle(new Point(e.getX(), e.getY()), height, width, border, fill));
                        repaint();
                    }
                    else if (selected instanceof Circle) {
                        if (selected instanceof Donut){
                            var params = BasicFormDialog.ask(
                                    SwingUtilities.getWindowAncestor(e.getComponent()),
                                    MODIFY_DONUT_FIELDS
                            );
                            if (params == null) return;

                            int x = (Integer) params.get("x");
                            int y = (Integer) params.get("y");
                            int inner = (Integer) params.get("inner");
                            int outer = (Integer) params.get("outer");
                            Color border = (Color) params.get("border");
                            Color fill   = (Color) params.get("fill");
                            shapes.remove(selected);
                            shapes.add(new Donut(new Point(x, y), outer, inner, border, fill));
                            repaint();
                        }
                        else {
                            var params = BasicFormDialog.ask(
                                    SwingUtilities.getWindowAncestor(e.getComponent()),
                                    MODIFY_CIRCLE_FIELDS
                            );
                            if (params == null) return;

                            int x = (Integer) params.get("x");
                            int y = (Integer) params.get("y");
                            int radius = (Integer) params.get("radius");
                            Color border = (Color) params.get("border");
                            Color fill   = (Color) params.get("fill");
                            shapes.remove(selected);
                            shapes.add(new Circle(new Point(x, y), radius, border, fill));
                            repaint();
                        }
                    }
                }
                if (mode == Modes.DELETE) {
                    Object selected = null;

                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        Shape s = shapes.get(i);

                        if (s.contains(new Point(e.getX(), e.getY())) && s.isSelected()) {
                            selected = s;
                            break;
                        }
                    }
                    if (selected == null) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(PnlDrawing.this),
                                "No shape selected. Select a shape first.",
                                "Nothing to delete",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    shapes.remove(selected);
                    repaint();
                }
            }
        });
    }

    public void setMode(Modes mode) {
        this.mode = mode;
        pendingShapeStart = null;

        if (mode != Modes.MODIFY && mode != Modes.DELETE) {
            shapes.forEach(s -> s.setSelected(false));
        }

        repaint();
    }

    public Shape getSelectedShape() {
        return shapes.stream()
                .filter(Shape::isSelected)
                .findFirst()
                .orElse(null);
    }

    public void chooseBorderColor() {
        Color c = JColorChooser.showDialog(this, "Choose border color", borderColor);
        if (c != null) borderColor = c;
    }

    public void chooseInnerColor() {
        Color c = JColorChooser.showDialog(this, "Choose inner color", innerColor);
        if (c != null) borderColor = c;
    }

//    public void undo() {
//        // if line start is pending, undo cancels that first
//        if (pendingShapeStart != null) {
//            pendingShapeStart = null;
//            repaint();
//            return;
//        }
//        if (!shapes.isEmpty()) {
//            shapes.remove(shapes.size() - 1);
//            repaint();
//        }
//    }
//
//    public void clearAll() {
//        shapes.clear();
//        pendingShapeStart = null;
//        repaint();
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // smoother rendering (small but nice)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Shape s : shapes) {
                if (s instanceof SurfaceShape ss) {
                    ss.fill(g2);
                    ss.draw(g2);
                }
                else {
                    s.draw(g2);
                }
                if (s.isSelected()) {
                    Color prevColor = g2.getColor();
                    Stroke prevStroke = g2.getStroke();

                    g2.setColor(Color.BLUE);
                    g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6, 4}, 0));

                    s.drawSelected(g2);

                    g2.setColor(prevColor);
                    g2.setStroke(prevStroke);
                }
            }

            // show pending line start
            if (mode == Modes.LINE && pendingShapeStart != null) {
                g2.setColor(new Color(120, 120, 120));
                g2.drawString("Line: click end point...", 10, 20);
                g2.fillOval(pendingShapeStart.getX() - 3, pendingShapeStart.getY() - 3, 6, 6);
            }
        } finally {
            g2.dispose();
        }
    }
}
