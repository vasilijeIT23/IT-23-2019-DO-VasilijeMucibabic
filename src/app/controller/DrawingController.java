package app.controller;

import app.command.*;
import app.drawing.components.BasicFormDialog;
import app.drawing.enums.Modes;
import app.geometry.*;
import app.geometry.Point;
import app.geometry.Rectangle;
import app.geometry.Shape;
import app.model.DrawingModel;
import app.observer.SelectionObserver;
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
import java.util.stream.Collectors;

import static app.drawing.components.FormLibrary.*;
import static app.drawing.components.FormLibrary.CREATE_CIRCLE_FIELDS;
import static app.drawing.components.FormLibrary.CREATE_DONUT_FIELDS;

public class DrawingController {

    // MVC
    private final DrawingModel model;
    private final DrawingView view;

    // State
    private Modes mode = Modes.POINT;
    private Point pendingShapeStart = null;

    // Strategies
    private SaveStrategy saveStrategy = new SaveLogStrategy();
    private LoadStrategy loadStrategy = null;

    // Constructor
    public DrawingController(DrawingModel model, DrawingView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }

    // Strategy setters
    public void setSaveStrategy(SaveStrategy strategy) { this.saveStrategy = strategy; }
    public void setLoadStrategy(LoadStrategy strategy) { this.loadStrategy = strategy; }
    public void save() { saveStrategy.save(model.getAllCommands()); }

    // Mode
    public Modes getMode() { return mode; }
    public Point getPendingShapeStart() { return pendingShapeStart; }

    public void setMode(Modes mode) {
        this.mode = mode;
        pendingShapeStart = null;
        if (mode != Modes.MODIFY && mode != Modes.DELETE) {
            model.getShapes().forEach(s -> s.setSelected(false));
            model.notifySelectionChanged();
        }
        view.repaint();
    }

    // Observer
    public void addSelectionObserver(SelectionObserver observer) {
        model.addSelectionObserver(observer);
    }

    // Helpers
    public List<Shape> getShapes() { return model.getShapes(); }
    public List<Shape> getShapesCopy() { return model.getShapesCopy(); }
    private Window getWindow() { return SwingUtilities.getWindowAncestor(view); }

    public void onActionSelected(Modes mode) {
        view.setStatus(mode.getStatusMessage());
        setMode(mode);
        if (mode == Modes.MODIFY) { handleModify(); }
        if (mode == Modes.DELETE) { handleDelete(); }
    }

    private Shape createPoint(MouseEvent e) {
        var params = BasicFormDialog.ask(getWindow(), CREATE_POINT_FIELDS);
        if (params == null) return null;
        Color color = (Color) params.get("border");
        return new Point(e.getX(), e.getY(), color);
    }

    private Shape createLine(MouseEvent e) {
        if (pendingShapeStart == null) {
            pendingShapeStart = new Point(e.getX(), e.getY());
            view.repaint();
            return null;
        }
        var params = BasicFormDialog.ask(getWindow(), CREATE_LINE_FIELDS);
        if (params == null) return null;
        Color color = (Color) params.get("border");
        Shape line = new Line(pendingShapeStart, new Point(e.getX(), e.getY()), color);
        pendingShapeStart = null;
        return line;
    }

    private Shape createRectangle(MouseEvent e) {
        var params = BasicFormDialog.ask(getWindow(), CREATE_RECTANGLE_FIELDS);
        if (params == null) return null;
        int width    = (Integer) params.get("width");
        int height   = (Integer) params.get("height");
        Color border = (Color)   params.get("border");
        Color fill   = (Color)   params.get("fill");
        return new Rectangle(new Point(e.getX(), e.getY()), height, width, border, fill);
    }

    private Shape createCircle(MouseEvent e) {
        var params = BasicFormDialog.ask(getWindow(), CREATE_CIRCLE_FIELDS);
        if (params == null) return null;
        int radius   = (Integer) params.get("radius");
        Color border = (Color)   params.get("border");
        Color fill   = (Color)   params.get("fill");
        return new Circle(new Point(e.getX(), e.getY()), radius, border, fill);
    }

    private Shape createDonut(MouseEvent e) {
        var params = BasicFormDialog.ask(getWindow(), CREATE_DONUT_FIELDS);
        if (params == null) return null;
        int inner    = (Integer) params.get("inner");
        int outer    = (Integer) params.get("outer");
        Color border = (Color)   params.get("border");
        Color fill   = (Color)   params.get("fill");
        return new Donut(new Point(e.getX(), e.getY()), outer, inner, border, fill);
    }

    private Shape createHexagon(MouseEvent e) {
        var params = BasicFormDialog.ask(getWindow(), CREATE_HEXAGON_FIELDS);
        if (params == null) return null;
        int radius   = (Integer) params.get("radius");
        Color border = (Color)   params.get("border");
        Color fill   = (Color)   params.get("fill");
        return new HexagonAdapter(e.getX(), e.getY(), radius, border, fill);
    }

    public void handleClick(MouseEvent e) {
        if (mode == Modes.SELECT) {
            for (int i = model.getSize() - 1; i >= 0; i--) {
                Shape s = model.getShape(i);
                if (s.contains(new Point(e.getX(), e.getY()))) {
                    s.setSelected(!s.isSelected());
                    break;
                }
            }
            model.notifySelectionChanged();
            view.repaint();
            return;
        }

        Shape shape = switch (mode) {
            case POINT -> createPoint(e);
            case LINE -> createLine(e);
            case RECTANGLE -> createRectangle(e);
            case CIRCLE -> createCircle(e);
            case DONUT -> createDonut(e);
            case HEXAGON -> createHexagon(e);
            default -> null;
        };

        if (shape == null) return;
        addShape(shape);
    }

    private void addShape(Shape shape) {
        model.executeCommand(new AddShapeCommand(getShapes(), shape, "Added " + shapeLogMessage(shape)));
        refresh();
    }

    private void modifyPoint(Point p) {
        var params = BasicFormDialog.ask(getWindow(), modifyPointFields(p));
        if (params == null) return;
        int x = (Integer) params.get("x");
        int y = (Integer) params.get("y");
        Color border = (Color) params.get("border");
        Shape modified = new Point(x, y, border);
        model.executeCommand(new ModifyShapeCommand(getShapes(), p, modified,
                "Modified Point from (" + p.getX() + ", " + p.getY() + ") to (" +
                        x + ", " + y + ") color=" + colorToString(border)));
        refresh();
    }

    private void modifyLine(Line l) {
        var params = BasicFormDialog.ask(getWindow(), modifyLineFields(l));
        if (params == null) return;
        int x  = (Integer) params.get("x");
        int y  = (Integer) params.get("y");
        int x2 = (Integer) params.get("x2");
        int y2 = (Integer) params.get("y2");
        Color border = (Color) params.get("border");
        Shape modified = new Line(new Point(x, y), new Point(x2, y2), border);
        model.executeCommand(new ModifyShapeCommand(getShapes(), l, modified,
                "Modified Line from (" + l.getA().getX() + ", " + l.getA().getY() + ")-("
                        + l.getB().getX() + ", " + l.getB().getY() + ") to (" + x + ", " + y
                        + ")-(" + x2 + ", " + y2 + ") color=" + colorToString(border)));
        refresh();
    }

    private void modifyRectangle(Rectangle r) {
        var params = BasicFormDialog.ask(getWindow(), modifyRectangleFields(r));
        if (params == null) return;
        int x      = (Integer) params.get("x");
        int y      = (Integer) params.get("y");
        int width  = (Integer) params.get("width");
        int height = (Integer) params.get("height");
        Color border = (Color) params.get("border");
        Color fill   = (Color) params.get("fill");
        Shape modified = new Rectangle(new Point(x, y), height, width, border, fill);
        model.executeCommand(new ModifyShapeCommand(getShapes(), r, modified,
                "Modified Rectangle from (" + r.getUpperLeftPoint().getX() + ", "
                        + r.getUpperLeftPoint().getY() + ") w=" + r.getWidth() + " h=" + r.getHeight() +
                        " to (" + x + ", " + y + ") w=" + width + " h=" + height + " border="
                        + colorToString(border) + " fill=" + colorToString(fill)));
        refresh();
    }
    private void modifyHexagon(HexagonAdapter h) {
        var params = BasicFormDialog.ask(getWindow(), modifyHexagonFields(h));
        if (params == null) return;
        int x      = (Integer) params.get("x");
        int y      = (Integer) params.get("y");
        int radius = (Integer) params.get("radius");
        Color border = (Color) params.get("border");
        Color fill   = (Color) params.get("fill");
        Shape modified = new HexagonAdapter(x, y, radius, border, fill);
        model.executeCommand(new ModifyShapeCommand(getShapes(), h, modified,
                "Modified Hexagon from (" + h.getX() + ", " + h.getY() + ") r=" + h.getRadius() +
                        " to (" + x + ", " + y + ") r=" + radius + " border=" + colorToString(border)
                        + " fill=" + colorToString(fill)));
        refresh();
    }
    private void modifyDonut(Donut d) {
        var params = BasicFormDialog.ask(getWindow(), modifyDonutFields(d));
        if (params == null) return;
        int x     = (Integer) params.get("x");
        int y     = (Integer) params.get("y");
        int inner = (Integer) params.get("inner");
        int outer = (Integer) params.get("outer");
        Color border = (Color) params.get("border");
        Color fill   = (Color) params.get("fill");
        Shape modified = new Donut(new Point(x, y), outer, inner, border, fill);
        model.executeCommand(new ModifyShapeCommand(getShapes(), d, modified,
                "Modified Donut from (" + d.getCenter().getX() + ", " + d.getCenter().getY()
                        + ") inner=" + d.getInnerRadius() + " outer=" + d.getRadius() + " to (" + x + ", "
                        + y + ") inner=" + inner + " outer=" + outer + " border=" + colorToString(border)
                        + " fill=" + colorToString(fill)));
        refresh();
    }

    private void modifyCircle(Circle c) {
        var params = BasicFormDialog.ask(getWindow(), modifyCircleFields(c));
        if (params == null) return;
        int x      = (Integer) params.get("x");
        int y      = (Integer) params.get("y");
        int radius = (Integer) params.get("radius");
        Color border = (Color) params.get("border");
        Color fill   = (Color) params.get("fill");
        Shape modified = new Circle(new Point(x, y), radius, border, fill);
        model.executeCommand(new ModifyShapeCommand(getShapes(), c, modified,
                "Modified Circle from (" + c.getCenter().getX() + ", "
                        + c.getCenter().getY() + ") r=" + c.getRadius() + " to (" + x + ", " + y + ") r="
                        + radius + " border=" + colorToString(border) + " fill=" + colorToString(fill)));
        refresh();
    }


    public void handleModify() {
        if (mode == Modes.MODIFY) {
            Shape selected = model.getSelectedShape();
            if (selected == null) { showNoSelectionWarning(); return; }

            if (selected instanceof Point p) { modifyPoint(p); }
            else if (selected instanceof Line l) { modifyLine(l); }
            else if (selected instanceof Rectangle r) { modifyRectangle(r); }
            else if (selected instanceof HexagonAdapter h) { modifyHexagon(h); }
            else if (selected instanceof Donut d) { modifyDonut(d); }
            else if (selected instanceof Circle c) { modifyCircle(c); }
        }
    }

    public void handleDelete() {
        if (mode == Modes.DELETE) {
            List<Shape> selected = model.getShapes().stream()
                    .filter(Shape::isSelected)
                    .toList();
            if (selected.isEmpty()) {
                showNoSelectionWarning();
                return;
            }
            List<Command> commands = new ArrayList<>();
            for (Shape shape : selected) {
                
                if (shape instanceof Point p) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(p)));
                } else if (shape instanceof Line l) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(l)));
                } else if (shape instanceof Rectangle r) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(r)));
                } else if (shape instanceof HexagonAdapter h) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(h)));
                } else if (shape instanceof Donut d) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(d)));
                } else if (shape instanceof Circle c) {
                    commands.add(new RemoveShapeCommand(getShapes(), shape, "Deleted " + shapeLogMessage(c)));
                }
            }
            String description = selected.stream()
                    .map(this::shapeLogMessage)
                    .collect(Collectors.joining(""));
            model.executeCommand(new CompositeCommand(commands, description));
            refresh();
        }
    }

    public void handleLogLoad(String mode, Shape shape, String message) {
        if (Objects.equals(mode, "D")) {
            model.executeCommand(new RemoveShapeCommand(getShapes(), shape, message));
        }
        else {
            model.executeCommand(new AddShapeCommand(getShapes(), shape, message));
        }
        refresh();
    }
    
    public void handleLogLoad(Shape shape, Shape newValues, String message) {
        model.executeCommand(new ModifyShapeCommand(getShapes(), shape, newValues, message));
        refresh();
    }

    public void handleLogLoad(ArrayList<Shape> shapes, String message) {
        List<Command> commands = new ArrayList<>();
        for (Shape shape : shapes) {
            commands.add(new RemoveShapeCommand(getShapes(), shape, message));
        }

        model.executeCommand(new CompositeCommand(commands, message));

    }

    private String colorToString(Color c) {
        if (c == null) return "none";
        return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
    }

    public void undo() {
        model.undo();
        refresh();
    }

    public void refresh() { view.refresh(); }

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

    public boolean canUndo() { return model.canUndo(); }

    public boolean canRedo() { return model.canRedo(); }

    public void load(JPanel canvas) {
        model.emptyList();
        model.clearLog();
        refresh();
        List<String> lines = loadStrategy.load(canvas);
        refresh();
        if (lines == null) return;

        LogReplayView replayView = new LogReplayView(canvas, model.getShapes());
        replayView.setController(this);
        replayView.replay(lines);
        refresh();
    }

    private void applyZOrder(ZAxisOrderCommand.ZOperation op, String prefix) {
        Shape selected = model.getSelectedShape();
        if (selected == null) { showNoSelectionWarning(); return; }
        model.executeCommand(new ZAxisOrderCommand(getShapes(), selected, op,
                prefix + shapeLogMessage(selected)));
        refresh();
    }

    public void toFront() { applyZOrder(ZAxisOrderCommand.ZOperation.TO_FRONT,"To Front: "); }
    public void toBack() { applyZOrder(ZAxisOrderCommand.ZOperation.TO_BACK,"To Back: "); }
    public void bringToFront() { applyZOrder(ZAxisOrderCommand.ZOperation.BRING_TO_FRONT,"Bring To Front: "); }
    public void bringToBack() { applyZOrder(ZAxisOrderCommand.ZOperation.BRING_TO_BACK, "Bring To Back: "); }

    private void applyZOrder(Shape shape, ZAxisOrderCommand.ZOperation op, String prefix) {
        model.executeCommand(new ZAxisOrderCommand(getShapes(), shape, op,
                prefix + shapeLogMessage(shape)));
        refresh();
    }

    public void toFront(Shape s) { applyZOrder(s, ZAxisOrderCommand.ZOperation.TO_FRONT, "To Front: "); }
    public void toBack(Shape s) { applyZOrder(s, ZAxisOrderCommand.ZOperation.TO_BACK, "To Back: "); }
    public void bringToFront(Shape s) { applyZOrder(s, ZAxisOrderCommand.ZOperation.BRING_TO_FRONT, "Bring To Front: "); }
    public void bringToBack(Shape s) { applyZOrder(s, ZAxisOrderCommand.ZOperation.BRING_TO_BACK,  "Bring To Back: "); }

    private void showNoSelectionWarning() {
        JOptionPane.showMessageDialog(getWindow(), "No shape selected. Select a shape first.",
                "Nothing selected", JOptionPane.WARNING_MESSAGE);
    }

    private String shapeLogMessage(Shape shape) {
        if (shape instanceof Point p)
            return "Point at (" + p.getX() + ", " + p.getY() + ") color=" + colorToString(p.getColor());
        if (shape instanceof Line l)
            return "Line from (" + l.getA().getX() + ", " + l.getA().getY() + ")-(" + l.getB().getX() + ", "
                    + l.getB().getY() + ") color=" + colorToString(l.getColor());
        if (shape instanceof Donut d)
            return "Donut at (" + d.getCenter().getX() + ", " + d.getCenter().getY() + ") inner=" + d.getInnerRadius()
                    + " outer=" + d.getRadius() + " border=" + colorToString(d.getColor())
                    + " fill=" + colorToString(d.getInnerColor()) + " ";
        if (shape instanceof Circle c)
            return "Circle at (" + c.getCenter().getX() + ", " + c.getCenter().getY() + ") r=" + c.getRadius()
                    + " border=" + colorToString(c.getColor()) + " fill=" + colorToString(c.getInnerColor()) + " ";
        if (shape instanceof Rectangle r)
            return "Rectangle at (" + r.getUpperLeftPoint().getX() + ", " + r.getUpperLeftPoint().getY() + ") w="
                    + r.getWidth() + " h=" + r.getHeight() + " border=" + colorToString(r.getColor()) + " fill="
                    + colorToString(r.getInnerColor()) + " ";
        if (shape instanceof HexagonAdapter h)
            return "Hexagon at (" + h.getX() + ", " + h.getY() + ") r=" + h.getRadius() + " border="
                    + colorToString(h.getColor()) + " fill=" + colorToString(h.getInnerColor()) + " ";
        return shape.getClass().getSimpleName();
    }
}