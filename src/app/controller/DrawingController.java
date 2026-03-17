package app.controller;

import app.drawing.components.BasicFormDialog;
import app.drawing.enums.Modes;
import app.geometry.Circle;
import app.geometry.Donut;
import app.geometry.Line;
import app.geometry.Point;
import app.geometry.Shape;
import app.model.DrawingModel;
import app.view.DrawingView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static app.drawing.components.FormLibrary.*;
import static app.drawing.components.FormLibrary.CREATE_CIRCLE_FIELDS;
import static app.drawing.components.FormLibrary.CREATE_DONUT_FIELDS;

public class DrawingController {
    public List<Shape> getShapes() {
        return model.getShapes();
    }

    private final DrawingModel model;
    private final DrawingView view;

    public DrawingController(DrawingModel model, DrawingView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }

    private Window getWindow() {
        return SwingUtilities.getWindowAncestor(view);
    }

    private Modes mode = Modes.POINT;

    public Point getPendingShapeStart() {
        return pendingShapeStart;
    }

    private Point pendingShapeStart = null;

    public void setMode(Modes mode) {
        this.mode = mode;
        pendingShapeStart = null;

        if (mode != Modes.MODIFY && mode != Modes.DELETE) {
            model.getShapes().forEach(s -> s.setSelected(false));
        }

        view.repaint();
    }

    public Modes getMode() {
        return mode;
    }

    public void onActionSelected(Modes mode) {
        view.setStatus(mode.getStatusMessage());
        setMode(mode);

        if (mode == Modes.MODIFY) {
            handleModify();
        }
    }

    public void handleModify() {
        if (mode == Modes.MODIFY) {
            Shape selected = null;

            for (int i = model.getSize() - 1; i >= 0; i--) {
                Shape s = model.getShape(i);
                if (s.isSelected()) {
                    selected = s;
                }
            }
            if (selected == null) {
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(view),
                        "No shape selected. Select a shape first.",
                        "Nothing to modify",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (selected instanceof app.geometry.Point p) {
                var params = BasicFormDialog.ask(getWindow(), modifyPointFields(p));
                if (params == null) return;

                int x = (Integer) params.get("x");
                int y = (Integer) params.get("y");
                Color border = (Color) params.get("border");
                model.removeShape(selected);
                model.addShape(new app.geometry.Point(x, y, border));
                view.repaint();
            }
            else if (selected instanceof Line l) {
                var params = BasicFormDialog.ask(getWindow(), modifyLineFields(l));
                if (params == null) return;

                int x = (Integer) params.get("x");
                int y = (Integer) params.get("y");
                int x2 = (Integer) params.get("x2");
                int y2 = (Integer) params.get("y2");
                Color border = (Color) params.get("border");
                model.removeShape(selected);
                model.addShape(new Line(new app.geometry.Point(x, y), new app.geometry.Point(x2, y2), border));
                view.repaint();
            }
            else if (selected instanceof app.geometry.Rectangle r) {
                var params = BasicFormDialog.ask(getWindow(), modifyRectangleFields(r));
                if (params == null) return;

                int x = (Integer) params.get("x");
                int y = (Integer) params.get("y");
                int width = (Integer) params.get("width");
                int height = (Integer) params.get("height");
                Color border = (Color) params.get("border");
                Color fill   = (Color) params.get("fill");
                model.removeShape(selected);
                model.addShape(new app.geometry.Rectangle(new Point(x, y), height, width, border, fill));
                view.repaint();
            }
            else if (selected instanceof Circle c) {
                if (selected instanceof Donut d){
                    var params = BasicFormDialog.ask(getWindow(), modifyDonutFields(d));
                    if (params == null) return;

                    int x = (Integer) params.get("x");
                    int y = (Integer) params.get("y");
                    int inner = (Integer) params.get("inner");
                    int outer = (Integer) params.get("outer");
                    Color border = (Color) params.get("border");
                    Color fill   = (Color) params.get("fill");
                    model.removeShape(selected);
                    model.addShape(new Donut(new app.geometry.Point(x, y), outer, inner, border, fill));
                    view.repaint();
                }
                else {
                    var params = BasicFormDialog.ask(getWindow(), modifyCircleFields(c));
                    if (params == null) return;

                    int x = (Integer) params.get("x");
                    int y = (Integer) params.get("y");
                    int radius = (Integer) params.get("radius");
                    Color border = (Color) params.get("border");
                    Color fill   = (Color) params.get("fill");
                    model.removeShape(selected);
                    model.addShape(new Circle(new app.geometry.Point(x, y), radius, border, fill));
                    view.repaint();
                }
            }
        }

    }

    public void handleClick(MouseEvent e) {
        if (mode == Modes.POINT) {
            var params = BasicFormDialog.ask(
                    SwingUtilities.getWindowAncestor(e.getComponent()),
                    CREATE_POINT_FIELDS
            );
            if (params == null) return;
            Color pointColor   = (Color) params.get("border");
            model.addShape(new app.geometry.Point(e.getX(), e.getY(), pointColor));
            view.repaint();
            return;
        }

        if (mode == Modes.LINE) {
            if (pendingShapeStart == null) {
                pendingShapeStart = new app.geometry.Point(e.getX(), e.getY());
                view.repaint();
            } else {
                var params = BasicFormDialog.ask(
                        SwingUtilities.getWindowAncestor(e.getComponent()),
                        CREATE_LINE_FIELDS
                );
                if (params == null) return;
                Color lineColor   = (Color) params.get("border");
                model.addShape(new Line(pendingShapeStart, new app.geometry.Point(e.getX(), e.getY()), lineColor));
                pendingShapeStart = null;
                view.repaint();
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
            model.addShape(new app.geometry.Rectangle(new app.geometry.Point(e.getX(), e.getY()), height, width, border, fill));
            view.repaint();
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
            model.addShape(new Circle(new app.geometry.Point(e.getX(), e.getY()), radius, border, fill));
            view.repaint();
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
            model.addShape(new Donut(new app.geometry.Point(e.getX(), e.getY()), outer, inner, border, fill));
            view.repaint();
        }

        if (mode == Modes.SELECT) {
            // deselect all first
            model.getShapes().forEach(s -> s.setSelected(false));

            for (int i = model.getSize() - 1; i >= 0; i--) {
                app.geometry.Shape s = model.getShape(i);
                if (s.contains(new app.geometry.Point(e.getX(), e.getY()))) {
                    s.setSelected(true);
                    break;
                }
            }
            view.repaint();
        }
        if (mode == Modes.DELETE) {
            Shape selected = null;

            for (int i = model.getSize() - 1; i >= 0; i--) {
                app.geometry.Shape s = model.getShape(i);

                if (s.contains(new Point(e.getX(), e.getY())) && s.isSelected()) {
                    selected = s;
                    break;
                }
            }
            if (selected == null) {
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(view),
                        "No shape selected. Select a shape first.",
                        "Nothing to delete",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            model.removeShape(selected);
            view.repaint();
        }
    }

}