package app.controller;

import app.command.*;
import app.drawing.components.BasicFormDialog;
import app.drawing.enums.Modes;
import app.geometry.*;
import app.geometry.Point;
import app.geometry.Rectangle;
import app.geometry.Shape;
import app.model.DrawingModel;
import app.strategy.LoadLogStrategy;
import app.strategy.LoadStrategy;
import app.strategy.SaveLogStrategy;
import app.strategy.SaveStrategy;
import app.view.DrawingView;
import app.view.LogReplayView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private SaveStrategy saveStrategy = new SaveLogStrategy(); // default

    public void setSaveStrategy(SaveStrategy strategy) {
        this.saveStrategy = strategy;
    }

    private LoadStrategy loadStrategy = null; // default

    public void setLoadStrategy(LoadStrategy strategy) {
        this.loadStrategy = strategy;
    }

    public void save() {
        saveStrategy.save(model.getAllCommands());
    }

    public List<Shape> getShapesCopy() {
        return model.getShapesCopy();
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

            if (selected instanceof Point p) {
                var params = BasicFormDialog.ask(getWindow(), modifyPointFields(p));
                if (params == null) return;
                int x = (Integer) params.get("x");
                int y = (Integer) params.get("y");
                Color border = (Color) params.get("border");
                Shape modified = new Point(x, y, border);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Point from (" + p.getX() + ", " + p.getY() + ") to (" + x + ", " + y + ") color=" + colorToString(border)));
                refresh();
            }
            else if (selected instanceof Line l) {
                var params = BasicFormDialog.ask(getWindow(), modifyLineFields(l));
                if (params == null) return;
                int x  = (Integer) params.get("x");
                int y  = (Integer) params.get("y");
                int x2 = (Integer) params.get("x2");
                int y2 = (Integer) params.get("y2");
                Color border = (Color) params.get("border");
                Shape modified = new Line(new Point(x, y), new Point(x2, y2), border);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Line from (" + l.getA().getX() + ", " + l.getA().getY() + ")-(" + l.getB().getX() + ", " + l.getB().getY() + ") to (" + x + ", " + y + ")-(" + x2 + ", " + y2 + ") color=" + colorToString(border)));
                refresh();
            }
            else if (selected instanceof Rectangle r) {
                var params = BasicFormDialog.ask(getWindow(), modifyRectangleFields(r));
                if (params == null) return;
                int x      = (Integer) params.get("x");
                int y      = (Integer) params.get("y");
                int width  = (Integer) params.get("width");
                int height = (Integer) params.get("height");
                Color border = (Color) params.get("border");
                Color fill   = (Color) params.get("fill");
                Shape modified = new Rectangle(new Point(x, y), height, width, border, fill);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Rectangle from (" + r.getUpperLeftPoint().getX() + ", " + r.getUpperLeftPoint().getY() + ") w=" + r.getWidth() + " h=" + r.getHeight() +
                                " to (" + x + ", " + y + ") w=" + width + " h=" + height + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
                refresh();
            }
            else if (selected instanceof HexagonAdapter h) {
                var params = BasicFormDialog.ask(getWindow(), modifyHexagonFields(h));
                if (params == null) return;
                int x      = (Integer) params.get("x");
                int y      = (Integer) params.get("y");
                int radius = (Integer) params.get("radius");
                Color border = (Color) params.get("border");
                Color fill   = (Color) params.get("fill");
                Shape modified = new HexagonAdapter(x, y, radius, border, fill);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Hexagon from (" + h.getX() + ", " + h.getY() + ") r=" + h.getRadius() +
                                " to (" + x + ", " + y + ") r=" + radius + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
                refresh();
            }
            else if (selected instanceof Donut d) {
                var params = BasicFormDialog.ask(getWindow(), modifyDonutFields(d));
                if (params == null) return;
                int x     = (Integer) params.get("x");
                int y     = (Integer) params.get("y");
                int inner = (Integer) params.get("inner");
                int outer = (Integer) params.get("outer");
                Color border = (Color) params.get("border");
                Color fill   = (Color) params.get("fill");
                Shape modified = new Donut(new Point(x, y), outer, inner, border, fill);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Donut from (" + d.getCenter().getX() + ", " + d.getCenter().getY() + ") inner=" + d.getInnerRadius() + " outer=" + d.getRadius() +
                                " to (" + x + ", " + y + ") inner=" + inner + " outer=" + outer + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
                refresh();
            }
            else if (selected instanceof Circle c) {
                var params = BasicFormDialog.ask(getWindow(), modifyCircleFields(c));
                if (params == null) return;
                int x      = (Integer) params.get("x");
                int y      = (Integer) params.get("y");
                int radius = (Integer) params.get("radius");
                Color border = (Color) params.get("border");
                Color fill   = (Color) params.get("fill");
                Shape modified = new Circle(new Point(x, y), radius, border, fill);
                model.executeCommand(new ModifyShapeCommand(getShapes(), selected, modified,
                        "Modified Circle from (" + c.getCenter().getX() + ", " + c.getCenter().getY() + ") r=" + c.getRadius() +
                                " to (" + x + ", " + y + ") r=" + radius + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
                refresh();
            }
        }
    }

    public void handleClick(MouseEvent e) {
        Shape shape = null;

        if (mode == Modes.POINT) {
            var params = BasicFormDialog.ask(getWindow(), CREATE_POINT_FIELDS);
            if (params == null) return;
            Color pointColor = (Color) params.get("border");
            shape = new Point(e.getX(), e.getY(), pointColor);
            model.executeCommand(new AddShapeCommand(getShapes(), shape,
                    "Added Point at (" + e.getX() + ", " + e.getY() + ") color=" + colorToString(pointColor)));
            refresh();
            return;
        }

        if (mode == Modes.LINE) {
            if (pendingShapeStart == null) {
                pendingShapeStart = new Point(e.getX(), e.getY());
                view.repaint();
            } else {
                var params = BasicFormDialog.ask(getWindow(), CREATE_LINE_FIELDS);
                if (params == null) return;
                Color lineColor = (Color) params.get("border");
                shape = new Line(pendingShapeStart, new Point(e.getX(), e.getY()), lineColor);
                model.executeCommand(new AddShapeCommand(getShapes(), shape,
                        "Added Line from (" + pendingShapeStart.getX() + ", " + pendingShapeStart.getY() + ") to (" + e.getX() + ", " + e.getY() + ") color=" + colorToString(lineColor)));
                pendingShapeStart = null;
                refresh();
            }
            return;
        }

        if (mode == Modes.RECTANGLE) {
            var params = BasicFormDialog.ask(getWindow(), CREATE_RECTANGLE_FIELDS);
            if (params == null) return;
            int width = (Integer) params.get("width");
            int height = (Integer) params.get("height");
            Color border = (Color) params.get("border");
            Color fill   = (Color) params.get("fill");
            shape = new Rectangle(new Point(e.getX(), e.getY()), height, width, border, fill);
            model.executeCommand(new AddShapeCommand(getShapes(), shape,
                    "Added Rectangle at (" + e.getX() + ", " + e.getY() + ") w=" + width + " h=" + height + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
            refresh();
            return;
        }

        if (mode == Modes.CIRCLE) {
            var params = BasicFormDialog.ask(getWindow(), CREATE_CIRCLE_FIELDS);
            if (params == null) return;
            int radius = (Integer) params.get("radius");
            Color border = (Color) params.get("border");
            Color fill = (Color) params.get("fill");
            shape = new Circle(new Point(e.getX(), e.getY()), radius, border, fill);
            model.executeCommand(new AddShapeCommand(getShapes(), shape,
                    "Added Circle at (" + e.getX() + ", " + e.getY() + ") r=" + radius + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
            refresh();
            return;
        }

        if (mode == Modes.DONUT) {
            var params = BasicFormDialog.ask(getWindow(), CREATE_DONUT_FIELDS);
            if (params == null) return;
            int inner = (Integer) params.get("inner");
            int outer = (Integer) params.get("outer");
            Color border = (Color) params.get("border");
            Color fill = (Color) params.get("fill");
            shape = new Donut(new Point(e.getX(), e.getY()), outer, inner, border, fill);
            model.executeCommand(new AddShapeCommand(getShapes(), shape,
                    "Added Donut at (" + e.getX() + ", " + e.getY() + ") inner=" + inner + " outer=" + outer + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
            refresh();
            return;
        }

        if (mode == Modes.HEXAGON) {
            var params = BasicFormDialog.ask(getWindow(), CREATE_HEXAGON_FIELDS);
            if (params == null) return;
            int radius = (Integer) params.get("radius");
            Color border = (Color) params.get("border");
            Color fill   = (Color) params.get("fill");
            shape = new HexagonAdapter(e.getX(), e.getY(), radius, border, fill);
            model.executeCommand(new AddShapeCommand(getShapes(), shape,
                    "Added Hexagon at (" + e.getX() + ", " + e.getY() + ") r=" + radius + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
            refresh();
            return;
        }

        if (mode == Modes.SELECT) {
            model.getShapes().forEach(s -> s.setSelected(false));
            for (int i = model.getSize() - 1; i >= 0; i--) {
                Shape s = model.getShape(i);
                if (s.contains(new Point(e.getX(), e.getY()))) {
                    s.setSelected(true);
                    break;
                }
            }
            view.repaint();
            return;
        }

        if (mode == Modes.DELETE) {
            Shape selected = null;
            for (int i = model.getSize() - 1; i >= 0; i--) {
                Shape s = model.getShape(i);
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
            if (selected instanceof Point p) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Point at (" + p.getX() + ", " + p.getY() + ")"));
                refresh();
            }
            else if (selected instanceof Line l) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Line from (" + l.getA().getX() + ", " + l.getA().getY() + ")-(" + l.getB().getX() + ", " + l.getB().getY() + ")"));
                refresh();
            }
            else if (selected instanceof Rectangle r) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Rectangle from (" + r.getUpperLeftPoint().getX() + ", " + r.getUpperLeftPoint().getY() + ") w=" + r.getWidth() + " h=" + r.getHeight()));
                refresh();
            }
            else if (selected instanceof HexagonAdapter h) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Hexagon from (" + h.getX() + ", " + h.getY() + ") r=" + h.getRadius()));
                refresh();
            }
            else if (selected instanceof Donut d) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Donut from (" + d.getCenter().getX() + ", " + d.getCenter().getY() + ") inner=" + d.getInnerRadius() + " outer=" + d.getRadius()));
                refresh();
            }
            else if (selected instanceof Circle c) {
                model.executeCommand(new RemoveShapeCommand(getShapes(), selected,
                        "Deleted Circle from (" + c.getCenter().getX() + ", " + c.getCenter().getY() + ") r=" + c.getRadius()));
                refresh();
            }
        }
    }

    public void handleLogLoad(String mode, Shape shape, String message) {
        if (Objects.equals(mode, "D")) {
            Shape selected = null;
            for (int i = model.getSize() - 1; i >= 0; i--) {
                Shape s = model.getShape(i);
                if (s.same(shape)) {
                    selected = s;
                }
            }
            model.executeCommand(new RemoveShapeCommand(getShapes(), selected, message));
        }
        else {
            model.executeCommand(new AddShapeCommand(getShapes(), shape, message));
        }
        refresh();
    }
    
    public void handleLogLoad(Shape shape, Shape newValues, String message) {
        Shape selected = null;
        for (int i = model.getSize() - 1; i >= 0; i--) {
            Shape s = model.getShape(i);
            if (s.same(shape)) {
                selected = s;
            }
        }
        model.executeCommand(new ModifyShapeCommand(getShapes(), selected, newValues, message));
    }

    private String colorToString(Color c) {
        if (c == null) return "none";
        return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
    }

    public void undo() {
        model.undo();
        refresh();
    }

    public void refresh() {
        view.refresh();
    }

    public void redo() {
        model.redo();
        refresh();
    }

    public List<String> getLog() {
        return model.getAllCommands()
                .stream()
                .map(Command::getDescription)
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean canUndo() {
        return model.canUndo();
    }

    public boolean canRedo() {
        return model.canRedo();
    }

    public void loadLog(JPanel canvas) {
        model.emptyList();
        model.clearLog();
        refresh();
        List<String> lines = loadStrategy.load(canvas);
        if (lines == null) return;

        LogReplayView replayView = new LogReplayView(canvas, model.getShapes());
        replayView.setController(this); // pass controller
        replayView.replay(lines);
        refresh();
    }
}