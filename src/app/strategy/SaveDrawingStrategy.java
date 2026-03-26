package app.strategy;

import app.command.Command;
import app.geometry.Shape;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveDrawingStrategy implements SaveStrategy {

    private final List<Shape> shapes;

    public SaveDrawingStrategy(List<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public void save(List<Command> logs) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save drawing");
        chooser.setSelectedFile(new File("drawing.bin"));

        int result = chooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(shapes));
            JOptionPane.showMessageDialog(null, "Drawing saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save drawing: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}