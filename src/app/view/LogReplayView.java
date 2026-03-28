package app.view;

import app.controller.DrawingController;
import app.geometry.Shape;
import app.strategy.LogParser;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogReplayView {

    private DrawingController controller;

    public void setController(DrawingController controller) {
        this.controller = controller;
    }

    private final JPanel parent;
    private final List<Shape> shapes;

    public LogReplayView(JPanel parent, List<Shape> shapes) {
        this.parent = parent;
        this.shapes = shapes;
    }

    public void replay(List<String> lines) {
        if (lines == null || lines.isEmpty()) return;

        for (String line : lines) {
            int choice = JOptionPane.showConfirmDialog(
                    parent,
                    "Next command:\n" + line + "\n\nExecute?",
                    "Replay Log",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (choice == JOptionPane.CANCEL_OPTION) break;
            if (choice == JOptionPane.NO_OPTION) continue;

            if (line.startsWith("Modified")) {
                ArrayList<Shape> applied = LogParser.modify(line, shapes);
                controller.handleLogLoad(applied.get(0), applied.get(1), line);
            } else if (line.startsWith("Deleted")) {
                if (line.indexOf("Deleted") != line.lastIndexOf("Deleted")) {
                    System.out.println(line);
                    ArrayList<Shape> toDelete = new ArrayList<>();
                    String[] parts = line.split("Deleted");
                    System.out.println(Arrays.toString(parts));
                    for (String part : parts) {
                        if (part.isBlank()) continue;
                        System.out.println("Deleted" + part);
                        toDelete.add(LogParser.delete("Deleted" + part, shapes));
                        System.out.println("Deleted" + part);
                    }
                    controller.handleLogLoad(toDelete, line);
                }
                else {
                    Shape shape = LogParser.delete(line, shapes);
                    controller.handleLogLoad("D", shape, line);
                }
            } else if (line.startsWith("Added")) {
                Shape shape = LogParser.add(line);
                controller.handleLogLoad("A", shape, line);
            } else if (line.startsWith("Undo")) {
                controller.undo();
            } else {
                controller.redo();
            }


            parent.repaint();
        }

        JOptionPane.showMessageDialog(parent, "Log replay complete.");
    }
}