// app/strategy/LoadDrawingStrategy.java
package app.strategy;

import app.geometry.Shape;

import javax.swing.*;
import java.io.*;
import java.util.List;

public class LoadDrawingStrategy implements LoadStrategy {

    private final List<Shape> shapes;

    public LoadDrawingStrategy(List<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public List<String> load(JPanel parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load drawing");

        int result = chooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File file = chooser.getSelectedFile();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Shape> loaded = (List<Shape>) ois.readObject();
            shapes.clear();
            shapes.addAll(loaded);
            JOptionPane.showMessageDialog(parent, "Drawing loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent, "Failed to load drawing: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null; // no lines to replay — drawing is loaded directly
    }
}